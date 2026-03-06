package com.hotel.system.dto;

import java.time.LocalDateTime;
import java.util.List;

// हा Bill Response Postman मध्ये clean दिसेल
public class BillResponse {

    private Long orderId;
    private int tableNumber;
    private String customerName;
    private String customerPhone;
    private LocalDateTime orderTime;
    private String status;
    private List<BillItem> items;
    private double totalAmount;

    // Bill मधील प्रत्येक Item
    public static class BillItem {
        private String itemName;
        private double price;
        private int quantity;
        private double subTotal;

        public BillItem(String itemName, double price, int quantity, double subTotal) {
            this.itemName = itemName;
            this.price = price;
            this.quantity = quantity;
            this.subTotal = subTotal;
        }

        public String getItemName() { return itemName; }
        public double getPrice() { return price; }
        public int getQuantity() { return quantity; }
        public double getSubTotal() { return subTotal; }
    }

    public BillResponse() {}

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public int getTableNumber() { return tableNumber; }
    public void setTableNumber(int tableNumber) { this.tableNumber = tableNumber; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }

    public LocalDateTime getOrderTime() { return orderTime; }
    public void setOrderTime(LocalDateTime orderTime) { this.orderTime = orderTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<BillItem> getItems() { return items; }
    public void setItems(List<BillItem> items) { this.items = items; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
}