package com.hotel.system.service;

import com.hotel.system.dto.BillResponse;
import com.hotel.system.entity.Order;
import com.hotel.system.entity.OrderItem;
import com.hotel.system.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BillService {

    private final OrderRepository orderRepository;

    public BillService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // Order ID वरून Bill तयार करणे
    public BillResponse generateBill(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        // FIX #16: CANCELLED order चा bill generate होऊ नये
        if ("CANCELLED".equals(order.getStatus())) {
            throw new RuntimeException("Cannot generate bill for a CANCELLED order!");
        }

        BillResponse bill = new BillResponse();

        // Order details
        bill.setOrderId(order.getId());
        bill.setOrderTime(order.getOrderTime());
        bill.setStatus(order.getStatus());
        bill.setTotalAmount(order.getTotalAmount());

        // Table details
        if (order.getTable() != null) {
            bill.setTableNumber(order.getTable().getTableNumber());
        }

        // Customer details
        if (order.getCustomer() != null) {
            bill.setCustomerName(order.getCustomer().getName());
            bill.setCustomerPhone(order.getCustomer().getPhone());
        }

        // Items details — काय काय order केलं
        List<BillResponse.BillItem> billItems = new ArrayList<>();
        for (OrderItem orderItem : order.getItems()) {
            BillResponse.BillItem billItem = new BillResponse.BillItem(
                    orderItem.getMenuItem().getName(),   // Item नाव
                    orderItem.getMenuItem().getPrice(),  // एकाची किंमत
                    orderItem.getQuantity(),             // किती quantity
                    orderItem.getPrice()                 // subtotal
            );
            billItems.add(billItem);
        }
        bill.setItems(billItems);

        return bill;
    }
}