package com.hotel.system.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "hotel_tables")
public class HotelTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer tableNumber;

    @Column(nullable = false)
    private Integer capacity;

    // ✅ floor field — Ground Floor / 1st Floor / 2nd Floor / Terrace
    @Column(nullable = false)
    private String floor = "Ground Floor";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TableStatus status = TableStatus.AVAILABLE;

    public enum TableStatus {
        AVAILABLE, OCCUPIED
    }

    public HotelTable() {}

    // ── Getters & Setters ──
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getTableNumber() { return tableNumber; }
    public void setTableNumber(Integer tableNumber) { this.tableNumber = tableNumber; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public String getFloor() { return floor; }
    public void setFloor(String floor) { this.floor = floor; }

    public TableStatus getStatus() { return status; }
    public void setStatus(TableStatus status) { this.status = status; }
}