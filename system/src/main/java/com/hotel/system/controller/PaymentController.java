package com.hotel.system.controller;

import com.hotel.system.dto.PaymentOrderRequest;
import com.hotel.system.dto.PaymentOrderResponse;
import com.hotel.system.dto.PaymentVerifyRequest;
import com.hotel.system.entity.Payment;
import com.hotel.system.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// NEW FILE — Payment Controller
// All endpoints under /payment
// Endpoints:
//   POST /payment/create-order  → Create Razorpay order, get popup config
//   POST /payment/verify        → Verify signature after customer pays
//   GET  /payment/status/{orderId} → Get payment status for an order
@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // ─── Create Razorpay Order ────────────────────────────────────────────────
    // Frontend calls this when cashier clicks "Pay via Razorpay"
    // Returns: razorpayOrderId, amount, currency, keyId — needed to open popup
    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestBody PaymentOrderRequest request) {
        try {
            PaymentOrderResponse response = paymentService.createRazorpayOrder(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ─── Verify Payment ──────────────────────────────────────────────────────
    // After customer pays on Razorpay popup, frontend sends signature here
    // Backend verifies HMAC-SHA256 and marks order as PAID
    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(@RequestBody PaymentVerifyRequest request) {
        try {
            String message = paymentService.verifyPayment(request);
            return ResponseEntity.ok(message);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ─── Get Payment Status ───────────────────────────────────────────────────
    // Check if payment exists for a given order
    @GetMapping("/status/{orderId}")
    public ResponseEntity<?> getPaymentStatus(@PathVariable Long orderId) {
        try {
            Payment payment = paymentService.getPaymentByOrderId(orderId);
            return ResponseEntity.ok(payment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}