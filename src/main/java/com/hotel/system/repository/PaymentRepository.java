package com.hotel.system.repository;

import com.hotel.system.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// NEW FILE — Repository for Payment entity
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Find payment by order id
    Optional<Payment> findByOrderId(Long orderId);

    // Find payment by Razorpay order id
    Optional<Payment> findByRazorpayOrderId(String razorpayOrderId);
}