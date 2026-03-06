package com.hotel.system.service;

import com.hotel.system.dto.OrderRequest;
import com.hotel.system.entity.*;
import com.hotel.system.repository.*;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuItemRepository menuItemRepository;
    private final CustomerRepository customerRepository;
    private final HotelTableRepository tableRepository;

    public OrderService(OrderRepository orderRepository,
                        MenuItemRepository menuItemRepository,
                        CustomerRepository customerRepository,
                        HotelTableRepository tableRepository) {
        this.orderRepository = orderRepository;
        this.menuItemRepository = menuItemRepository;
        this.customerRepository = customerRepository;
        this.tableRepository = tableRepository;
    }

    // ORDER CREATE
    public Order createOrder(OrderRequest request) {

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new RuntimeException("Order must contain at least one item");
        }

        Order order = new Order();

        // Customer link
        if (request.getCustomerId() != null) {
            Customer customer = customerRepository.findById(request.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found: " + request.getCustomerId()));
            order.setCustomer(customer);
        }

        // Table link
        if (request.getTableId() != null) {
            HotelTable table = tableRepository.findById(request.getTableId())
                    .orElseThrow(() -> new RuntimeException("Table not found: " + request.getTableId()));

            if (table.getStatus().equals("OCCUPIED")) {
                throw new RuntimeException("Table " + table.getTableNumber() + " is already occupied!");
            }

            table.setStatus("OCCUPIED");
            tableRepository.save(table);
            order.setTable(table);
        }

        List<OrderItem> orderItems = new ArrayList<>();
        double total = 0;

        for (OrderRequest.ItemRequest itemReq : request.getItems()) {

            if (itemReq.getMenuItemId() == null) {
                throw new RuntimeException("menuItemId cannot be null");
            }

            MenuItem menuItem = menuItemRepository.findById(itemReq.getMenuItemId())
                    .orElseThrow(() -> new RuntimeException("Menu item not found: " + itemReq.getMenuItemId()));

            if (!menuItem.isAvailable()) {
                throw new RuntimeException("Menu item not available: " + menuItem.getName());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setMenuItem(menuItem);
            orderItem.setQuantity(itemReq.getQuantity());
            double price = menuItem.getPrice() * itemReq.getQuantity();
            orderItem.setPrice(price);
            orderItem.setOrder(order);

            total += price;
            orderItems.add(orderItem);
        }

        order.setItems(orderItems);
        order.setTotalAmount(total);
        order.setStatus("PENDING");

        return orderRepository.save(order);
    }

    // GET ORDER BY ID
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
    }

    // ORDER STATUS UPDATE
    public Order updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        order.setStatus(status);

        // PAID झाल्यावर Table AVAILABLE होते
        if (status.equals("PAID") && order.getTable() != null) {
            HotelTable table = order.getTable();
            table.setStatus("AVAILABLE");
            tableRepository.save(table);
        }

        return orderRepository.save(order);
    }

    // CANCEL ORDER — फक्त PENDING order cancel करता येईल
    public String cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        // PAID order cancel करता येणार नाही
        if (order.getStatus().equals("PAID")) {
            throw new RuntimeException("Cannot cancel a PAID order!");
        }

        // SERVED order cancel करता येणार नाही
        if (order.getStatus().equals("SERVED")) {
            throw new RuntimeException("Cannot cancel a SERVED order!");
        }

        // Table AVAILABLE करणे
        if (order.getTable() != null) {
            HotelTable table = order.getTable();
            table.setStatus("AVAILABLE");
            tableRepository.save(table);
        }

        // Order CANCELLED mark करणे
        order.setStatus("CANCELLED");
        orderRepository.save(order);

        return "Order #" + orderId + " cancelled successfully";
    }

    // KITCHEN VIEW — PENDING orders
    public List<Order> getPendingOrders() {
        return orderRepository.findByStatus("PENDING");
    }

    // CUSTOMER ORDER HISTORY
    public List<Order> getOrdersByCustomer(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    // TABLE ORDERS
    public List<Order> getOrdersByTable(Long tableId) {
        return orderRepository.findByTableId(tableId);
    }

    // सगळे orders
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // PAID orders
    public List<Order> getPaidOrders() {
        return orderRepository.findByStatus("PAID");
    }
}