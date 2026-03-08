package com.hotel.system.controller;

import com.hotel.system.dto.SalesReportResponse;
import com.hotel.system.service.SalesReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/report")
// FIX #5: @CrossOrigin("*") काढला — CorsConfig.java globally handle करतो
public class SalesReportController {

    private final SalesReportService reportService;

    public SalesReportController(SalesReportService reportService) {
        this.reportService = reportService;
    }

    // आजचा Sales Report
    @GetMapping("/today")
    public ResponseEntity<SalesReportResponse> getTodayReport() {
        return ResponseEntity.ok(reportService.getTodayReport());
    }

    // कोणत्याही दिवसाचा Report — /report/date?date=2026-03-04
    @GetMapping("/date")
    public ResponseEntity<?> getReportByDate(@RequestParam String date) {
        try {
            LocalDate localDate = LocalDate.parse(date);
            return ResponseEntity.ok(reportService.getReportByDate(localDate));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid date format. Use: YYYY-MM-DD");
        }
    }
}