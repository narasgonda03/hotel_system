package com.hotel.system.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// NEW FILE — Payment entity to track every payment transaction
// Linked to Order; one order has one payment record
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Razorpay order id (returned when creating order on Razorpay)
    private String razorpayOrderId;

    // Razorpay payment id (returned after customer pays)
    private String razorpayPaymentId;

    // Razorpay signature (for verification)
    private String razorpaySignature;

    // Amount in paise (Razorpay uses smallest currency unit)
    private Long amountInPaise;

    // Currency — INR for India
    private String currency = "INR";

    // CREATED | PAID | FAILED
    private String status = "CREATED";

    // Payment mode: CASH | CARD | UPI | WALLET | ONLINE
    private String paymentMode;

    private LocalDateTime createdAt;
    private LocalDateTime paidAt;

    // Link to the Order
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    public Payment() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }

    public String getRazorpayOrderId() { return razorpayOrderId; }
    public void setRazorpayOrderId(String razorpayOrderId) { this.razorpayOrderId = razorpayOrderId; }

    public String getRazorpayPaymentId() { return razorpayPaymentId; }
    public void setRazorpayPaymentId(String razorpayPaymentId) { this.razorpayPaymentId = razorpayPaymentId; }

    public String getRazorpaySignature() { return razorpaySignature; }
    public void setRazorpaySignature(String razorpaySignature) { this.razorpaySignature = razorpaySignature; }

    public Long getAmountInPaise() { return amountInPaise; }
    public void setAmountInPaise(Long amountInPaise) { this.amountInPaise = amountInPaise; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPaymentMode() { return paymentMode; }
    public void setPaymentMode(String paymentMode) { this.paymentMode = paymentMode; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getPaidAt() { return paidAt; }
    public void setPaidAt(LocalDateTime paidAt) { this.paidAt = paidAt; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
}