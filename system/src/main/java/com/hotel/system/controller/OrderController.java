package com.hotel.system.controller;

import com.hotel.system.entity.Order;
import com.hotel.system.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@CrossOrigin("*")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public Order createOrder(@RequestBody Order order) {
        return service.createOrder(order);
    }

    @GetMapping("/all")
    public List<Order> getOrders() {
        return service.getAllOrders();
    }
}
