package com.hotel.system.dto;

// NEW FILE — Request DTO: frontend sends orderId to create Razorpay payment order
public class PaymentOrderRequest {

    private Long orderId;       // Our hotel order id
    private String paymentMode; // UPI | CARD | ONLINE | etc.

    public PaymentOrderRequest() {}

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public String getPaymentMode() { return paymentMode; }
    public void setPaymentMode(String paymentMode) { this.paymentMode = paymentMode; }
}