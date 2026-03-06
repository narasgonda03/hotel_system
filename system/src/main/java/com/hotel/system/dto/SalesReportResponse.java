package com.hotel.system.dto;

import java.time.LocalDate;
import java.util.List;

public class SalesReportResponse {

    private LocalDate date;
    private int totalOrders;
    private int paidOrders;
    private int pendingOrders;
    private double totalRevenue;
    private List<TopItem> topItems; // सर्वात जास्त order झालेले items

    // Top Selling Item
    public static class TopItem {
        private String itemName;
        private int totalQuantity;
        private double totalRevenue;

        public TopItem(String itemName, int totalQuantity, double totalRevenue) {
            this.itemName = itemName;
            this.totalQuantity = totalQuantity;
            this.totalRevenue = totalRevenue;
        }

        public String getItemName() { return itemName; }
        public int getTotalQuantity() { return totalQuantity; }
        public double getTotalRevenue() { return totalRevenue; }
    }

    public SalesReportResponse() {}

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public int getTotalOrders() { return totalOrders; }
    public void setTotalOrders(int totalOrders) { this.totalOrders = totalOrders; }

    public int getPaidOrders() { return paidOrders; }
    public void setPaidOrders(int paidOrders) { this.paidOrders = paidOrders; }

    public int getPendingOrders() { return pendingOrders; }
    public void setPendingOrders(int pendingOrders) { this.pendingOrders = pendingOrders; }

    public double getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(double totalRevenue) { this.totalRevenue = totalRevenue; }

    public List<TopItem> getTopItems() { return topItems; }
    public void setTopItems(List<TopItem> topItems) { this.topItems = topItems; }
}