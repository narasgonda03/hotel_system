package com.hotel.system.dto;

// NEW FILE — Response DTO: backend returns this after creating a Razorpay order
// Frontend uses these values to open Razorpay checkout popup
public class PaymentOrderResponse {

    private String razorpayOrderId;   // rzp order id e.g. order_XXXXXXXX
    private Long amountInPaise;       // Amount in paise (₹100 = 10000 paise)
    private String currency;          // INR
    private String keyId;             // Razorpay Key ID (public — safe to send to frontend)
    private Long hotelOrderId;        // Our internal order id
    private String customerName;      // Pre-fill in Razorpay popup
    private String customerPhone;     // Pre-fill in Razorpay popup

    public PaymentOrderResponse() {}

    public String getRazorpayOrderId() { return razorpayOrderId; }
    public void setRazorpayOrderId(String razorpayOrderId) { this.razorpayOrderId = razorpayOrderId; }

    public Long getAmountInPaise() { return amountInPaise; }
    public void setAmountInPaise(Long amountInPaise) { this.amountInPaise = amountInPaise; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getKeyId() { return keyId; }
    public void setKeyId(String keyId) { this.keyId = keyId; }

    public Long getHotelOrderId() { return hotelOrderId; }
    public void setHotelOrderId(Long hotelOrderId) { this.hotelOrderId = hotelOrderId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }
}