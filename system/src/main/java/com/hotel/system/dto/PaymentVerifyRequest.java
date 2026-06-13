package com.hotel.system.dto;

// NEW FILE — Request DTO: frontend sends these 3 values after Razorpay payment succeeds
// These are used to verify signature on backend
public class PaymentVerifyRequest {

    private String razorpayOrderId;    // rzp order id
    private String razorpayPaymentId;  // rzp payment id (after payment)
    private String razorpaySignature;  // HMAC-SHA256 signature to verify
    private Long hotelOrderId;         // Our internal order id

    public PaymentVerifyRequest() {}

    public String getRazorpayOrderId() { return razorpayOrderId; }
    public void setRazorpayOrderId(String razorpayOrderId) { this.razorpayOrderId = razorpayOrderId; }

    public String getRazorpayPaymentId() { return razorpayPaymentId; }
    public void setRazorpayPaymentId(String razorpayPaymentId) { this.razorpayPaymentId = razorpayPaymentId; }

    public String getRazorpaySignature() { return razorpaySignature; }
    public void setRazorpaySignature(String razorpaySignature) { this.razorpaySignature = razorpaySignature; }

    public Long getHotelOrderId() { return hotelOrderId; }
    public void setHotelOrderId(Long hotelOrderId) { this.hotelOrderId = hotelOrderId; }
}