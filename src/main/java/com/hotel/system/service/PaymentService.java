package com.hotel.system.service;

import com.hotel.system.dto.PaymentOrderRequest;
import com.hotel.system.dto.PaymentOrderResponse;
import com.hotel.system.dto.PaymentVerifyRequest;
import com.hotel.system.entity.Order;
import com.hotel.system.entity.Payment;
import com.hotel.system.repository.OrderRepository;
import com.hotel.system.repository.PaymentRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HexFormat;

// NEW FILE — Payment Service
// Uses Razorpay REST API directly via HttpClient (no extra SDK needed)
// Flow: createRazorpayOrder → customer pays → verifyPayment → mark order PAID
@Service
public class PaymentService {

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final OrderService orderService;

    public PaymentService(OrderRepository orderRepository,
                          PaymentRepository paymentRepository,
                          OrderService orderService) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
        this.orderService = orderService;
    }

    // ─── STEP 1: Create Razorpay Order ───────────────────────────────────────
    // Called when cashier clicks "Pay Online" — returns details for Razorpay popup
    public PaymentOrderResponse createRazorpayOrder(PaymentOrderRequest request) {
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found: " + request.getOrderId()));

        if ("CANCELLED".equals(order.getStatus())) {
            throw new RuntimeException("Cannot create payment for a CANCELLED order!");
        }
        if ("PAID".equals(order.getStatus())) {
            throw new RuntimeException("Order is already PAID!");
        }

        // GST 5% included — match Bill.js frontend calculation
        double gst = (order.getTotalAmount() * 5.0) / 100.0;
        double grandTotal = order.getTotalAmount() + gst;

        // Razorpay uses amount in paise (1 INR = 100 paise)
        long amountInPaise = Math.round(grandTotal * 100);

        try {
            // Build Razorpay create order request body
            JSONObject orderBody = new JSONObject();
            orderBody.put("amount", amountInPaise);
            orderBody.put("currency", "INR");
            orderBody.put("receipt", "hotel_order_" + order.getId());
            orderBody.put("payment_capture", 1); // Auto capture payment

            // Call Razorpay Orders API
            java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
            String credentials = java.util.Base64.getEncoder()
                    .encodeToString((keyId + ":" + keySecret).getBytes(StandardCharsets.UTF_8));

            java.net.http.HttpRequest httpRequest = java.net.http.HttpRequest.newBuilder()
                    .uri(java.net.URI.create("https://api.razorpay.com/v1/orders"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Basic " + credentials)
                    .POST(java.net.http.HttpRequest.BodyPublishers.ofString(orderBody.toString()))
                    .build();

            java.net.http.HttpResponse<String> response = client.send(
                    httpRequest, java.net.http.HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Razorpay API error: " + response.body());
            }

            JSONObject rzpResponse = new JSONObject(response.body());
            String rzpOrderId = rzpResponse.getString("id");

            // Save Payment record in DB
            Payment payment = new Payment();
            payment.setOrder(order);
            payment.setRazorpayOrderId(rzpOrderId);
            payment.setAmountInPaise(amountInPaise);
            payment.setCurrency("INR");
            payment.setStatus("CREATED");
            payment.setPaymentMode(request.getPaymentMode() != null ? request.getPaymentMode() : "ONLINE");
            paymentRepository.save(payment);

            // Build response for frontend
            PaymentOrderResponse resp = new PaymentOrderResponse();
            resp.setRazorpayOrderId(rzpOrderId);
            resp.setAmountInPaise(amountInPaise);
            resp.setCurrency("INR");
            resp.setKeyId(keyId);               // Public key — safe to send
            resp.setHotelOrderId(order.getId());
            if (order.getCustomer() != null) {
                resp.setCustomerName(order.getCustomer().getName());
                resp.setCustomerPhone(order.getCustomer().getPhone());
            }
            return resp;

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create Razorpay order: " + e.getMessage(), e);
        }
    }

    // ─── STEP 2: Verify Payment After Customer Pays ───────────────────────────
    // Razorpay sends razorpayOrderId + razorpayPaymentId + razorpaySignature to frontend
    // Frontend sends these to backend; we verify HMAC-SHA256 signature
    public String verifyPayment(PaymentVerifyRequest request) {
        try {
            // Signature = HMAC-SHA256(razorpayOrderId + "|" + razorpayPaymentId, keySecret)
            String data = request.getRazorpayOrderId() + "|" + request.getRazorpayPaymentId();

            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(
                    keySecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKeySpec);
            byte[] hashBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            String generatedSignature = HexFormat.of().formatHex(hashBytes);

            if (!generatedSignature.equals(request.getRazorpaySignature())) {
                // Signature mismatch — payment tampered or invalid
                throw new RuntimeException("Payment verification failed! Invalid signature.");
            }

            // Signature verified — update Payment record
            Payment payment = paymentRepository
                    .findByRazorpayOrderId(request.getRazorpayOrderId())
                    .orElseThrow(() -> new RuntimeException("Payment record not found!"));

            payment.setRazorpayPaymentId(request.getRazorpayPaymentId());
            payment.setRazorpaySignature(request.getRazorpaySignature());
            payment.setStatus("PAID");
            payment.setPaidAt(LocalDateTime.now());
            paymentRepository.save(payment);

            // Mark the hotel Order as PAID
            orderService.updateOrderStatus(request.getHotelOrderId(), "PAID");

            return "Payment verified successfully! Order marked as PAID.";

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Signature verification error: " + e.getMessage(), e);
        }
    }

    // ─── Get Payment status by order id ──────────────────────────────────────
    public Payment getPaymentByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("No payment found for order: " + orderId));
    }
}