package com.hotel.system.repository;

import com.hotel.system.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // Status नुसार orders
    List<Order> findByStatus(String status);

    // Customer नुसार orders — Customer History साठी
    List<Order> findByCustomerId(Long customerId);

    // Table नुसार orders
    List<Order> findByTableId(Long tableId);

    // आजचे orders — Daily Report साठी
    List<Order> findByOrderTimeBetween(LocalDateTime start, LocalDateTime end);
}