package com.hotel.system.service;

import com.hotel.system.dto.OrderRequest;
import com.hotel.system.entity.*;
import com.hotel.system.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    // FIX #7: @Transactional — Order create + Table status update एकाच transaction मध्ये
    // जर कुठेही error आला तर दोन्ही rollback होतील — data inconsistency नाही
    @Transactional
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

            if (table.getStatus() == HotelTable.TableStatus.OCCUPIED) {
                throw new RuntimeException("Table " + table.getTableNumber() + " is already occupied!");
            }

            table.setStatus(HotelTable.TableStatus.OCCUPIED);
            tableRepository.save(table);
            order.setTable(table);
        }

        List<OrderItem> orderItems = new ArrayList<>();
        double total = 0;

        for (OrderRequest.ItemRequest itemReq : request.getItems()) {

            if (itemReq.getMenuItemId() == null) {
                throw new RuntimeException("menuItemId cannot be null");
            }

            // FIX #14: Quantity validation — zero किंवा negative quantity allow नाही
            if (itemReq.getQuantity() <= 0) {
                throw new RuntimeException("Quantity must be at least 1 for each item");
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

    // FIX #6: Order Status Transition Validation — invalid transitions block करा
    // Valid flow: PENDING → PREPARING → SERVED → PAID
    // FIX #7: @Transactional — status + table update एकाच transaction मध्ये
    @Transactional
    public Order updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        String currentStatus = order.getStatus();

        // FIX #6: Valid status transitions enforce करणे
        validateStatusTransition(currentStatus, status);

        order.setStatus(status);

        // PAID झाल्यावर Table AVAILABLE — enum use
        if (status.equals("PAID") && order.getTable() != null) {
            HotelTable table = order.getTable();
            table.setStatus(HotelTable.TableStatus.AVAILABLE);
            tableRepository.save(table);
        }

        return orderRepository.save(order);
    }

    // FIX #6: Status transition rules define केल्या
    private void validateStatusTransition(String current, String next) {
        boolean valid = false;
        switch (current) {
            case "PENDING":
                // PENDING → PREPARING (Kitchen flow)
                // PENDING → PAID (Cashier directly collects — Bill page)
                // PENDING → CANCELLED (Cancel order)
                valid = next.equals("PREPARING") || next.equals("PAID") || next.equals("CANCELLED");
                break;
            case "PREPARING":
                // PREPARING → SERVED (Kitchen done)
                // PREPARING → PAID (Direct payment)
                valid = next.equals("SERVED") || next.equals("PAID");
                break;
            case "SERVED":
                // SERVED → PAID (Cashier collects payment)
                valid = next.equals("PAID");
                break;
            case "PAID":
            case "CANCELLED":
                valid = false; // Final states — change नाही होणार
                break;
        }
        if (!valid) {
            throw new RuntimeException("Invalid status transition: " + current + " → " + next);
        }
    }

    // FIX #7: @Transactional — cancel + table update एकाच transaction मध्ये
    @Transactional
    public String cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        if (order.getStatus().equals("PAID")) {
            throw new RuntimeException("Cannot cancel a PAID order!");
        }

        if (order.getStatus().equals("SERVED")) {
            throw new RuntimeException("Cannot cancel a SERVED order!");
        }

        if (order.getTable() != null) {
            HotelTable table = order.getTable();
            table.setStatus(HotelTable.TableStatus.AVAILABLE);
            tableRepository.save(table);
        }

        order.setStatus("CANCELLED");
        orderRepository.save(order);

        return "Order #" + orderId + " cancelled successfully";
    }

    // KITCHEN VIEW — PENDING orders
    public List<Order> getPendingOrders() {
        return orderRepository.findByStatus("PENDING");
    }

    // FIX #11: Kitchen साठी PREPARING orders — वेगळा method
    public List<Order> getPreparingOrders() {
        return orderRepository.findByStatus("PREPARING");
    }

    // FIX #11: Kitchen साठी SERVED orders — वेगळा method
    public List<Order> getServedOrders() {
        return orderRepository.findByStatus("SERVED");
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