package com.hotel.system.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Customer link
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = true)
    private Customer customer;

    // Table link — कोणत्या table चा order
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "table_id", nullable = true)
    private HotelTable table;

    private double totalAmount;
    private LocalDateTime orderTime;

    // Order Status Flow: PENDING → PREPARING → SERVED → PAID
    private String status = "PENDING";

    // Order Items — Bill मध्ये दाखवण्यासाठी JsonIgnore काढला
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderItem> items;

    public Order() {
        this.orderTime = LocalDateTime.now();
    }

    public Long getId() { return id; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public HotelTable getTable() { return table; }
    public void setTable(HotelTable table) { this.table = table; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public LocalDateTime getOrderTime() { return orderTime; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}