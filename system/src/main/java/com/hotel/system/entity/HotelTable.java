package com.hotel.system.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "hotel_tables")
public class HotelTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int tableNumber;
    private int capacity;
    private String status; // AVAILABLE / OCCUPIED

    public HotelTable() {}

    public Long getId() { return id; }

    public int getTableNumber() { return tableNumber; }
    public void setTableNumber(int tableNumber) { this.tableNumber = tableNumber; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}