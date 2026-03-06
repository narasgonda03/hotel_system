package com.hotel.system.service;

import com.hotel.system.dto.SalesReportResponse;
import com.hotel.system.entity.Order;
import com.hotel.system.entity.OrderItem;
import com.hotel.system.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SalesReportService {

    private final OrderRepository orderRepository;

    public SalesReportService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // आजचा Report
    public SalesReportResponse getTodayReport() {
        LocalDate today = LocalDate.now();
        return getReportByDate(today);
    }

    // कोणत्याही दिवसाचा Report — date: 2026-03-04
    public SalesReportResponse getReportByDate(LocalDate date) {

        LocalDateTime start = date.atStartOfDay();           // 00:00:00
        LocalDateTime end = date.atTime(23, 59, 59);         // 23:59:59

        List<Order> orders = orderRepository.findByOrderTimeBetween(start, end);

        SalesReportResponse report = new SalesReportResponse();
        report.setDate(date);
        report.setTotalOrders(orders.size());

        int paidCount = 0;
        int pendingCount = 0;
        double totalRevenue = 0;

        // Item wise count करण्यासाठी map
        Map<String, int[]> itemMap = new HashMap<>();
        // itemMap = { "Paneer Tikka": [quantity, revenue] }

        for (Order order : orders) {

            if (order.getStatus().equals("PAID")) {
                paidCount++;
                totalRevenue += order.getTotalAmount();
            } else {
                pendingCount++;
            }

            // प्रत्येक item count करणे
            if (order.getItems() != null) {
                for (OrderItem item : order.getItems()) {
                    String itemName = item.getMenuItem().getName();
                    int qty = item.getQuantity();
                    double rev = item.getPrice();

                    if (itemMap.containsKey(itemName)) {
                        itemMap.get(itemName)[0] += qty;
                        itemMap.get(itemName)[1] += (int) rev;
                    } else {
                        itemMap.put(itemName, new int[]{qty, (int) rev});
                    }
                }
            }
        }

        report.setPaidOrders(paidCount);
        report.setPendingOrders(pendingCount);
        report.setTotalRevenue(totalRevenue);

        // Top Items list तयार करणे
        List<SalesReportResponse.TopItem> topItems = new ArrayList<>();
        for (Map.Entry<String, int[]> entry : itemMap.entrySet()) {
            topItems.add(new SalesReportResponse.TopItem(
                    entry.getKey(),
                    entry.getValue()[0],
                    entry.getValue()[1]
            ));
        }

        // सर्वात जास्त order झालेले items आधी दाखवणे
        topItems.sort((a, b) -> b.getTotalQuantity() - a.getTotalQuantity());
        report.setTopItems(topItems);

        return report;
    }
}