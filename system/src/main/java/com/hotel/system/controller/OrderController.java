package com.hotel.system.controller;

import com.hotel.system.dto.OrderRequest;
import com.hotel.system.entity.Order;
import com.hotel.system.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/order")
@CrossOrigin("*")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    // Order Create
    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.createOrder(request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // GET ORDER BY ID — Single order बघणे
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.getOrderById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Order Status Update — PENDING → PREPARING → SERVED → PAID
    @PutMapping("/status/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable Long id,
                                          @RequestParam String status) {
        try {
            return ResponseEntity.ok(service.updateOrderStatus(id, status));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // CANCEL ORDER — फक्त PENDING/PREPARING cancel होईल
    @PutMapping("/cancel/{id}")
    public ResponseEntity<?> cancelOrder(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.cancelOrder(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Kitchen View — PENDING orders
    @GetMapping("/kitchen")
    public List<Order> getKitchenOrders() {
        return service.getPendingOrders();
    }

    // Customer Order History
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<?> getOrdersByCustomer(@PathVariable Long customerId) {
        try {
            return ResponseEntity.ok(service.getOrdersByCustomer(customerId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Table Orders
    @GetMapping("/table/{tableId}")
    public ResponseEntity<?> getOrdersByTable(@PathVariable Long tableId) {
        try {
            return ResponseEntity.ok(service.getOrdersByTable(tableId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // सगळे orders
    @GetMapping("/all")
    public List<Order> getAllOrders() {
        return service.getAllOrders();
    }

    // PAID orders
    @GetMapping("/paid")
    public List<Order> getPaidOrders() {
        return service.getPaidOrders();
    }
}