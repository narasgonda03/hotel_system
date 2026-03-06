package com.hotel.system.controller;

import com.hotel.system.dto.BillResponse;
import com.hotel.system.service.BillService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bill")
@CrossOrigin("*")
public class BillController {

    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }

    // Order ID वरून Bill generate करा
    @GetMapping("/{orderId}")
    public ResponseEntity<?> getBill(@PathVariable Long orderId) {
        try {
            BillResponse bill = billService.generateBill(orderId);
            return ResponseEntity.ok(bill);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}