package com.hotel.system.controller;

import com.hotel.system.entity.HotelTable;
import com.hotel.system.service.HotelTableService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/tables")
@CrossOrigin("*")
public class HotelTableController {

    private final HotelTableService service;

    public HotelTableController(HotelTableService service) {
        this.service = service;
    }

    // Add Table
    @PostMapping("/add")
    public ResponseEntity<HotelTable> addTable(@RequestBody HotelTable table) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addTable(table));
    }

    // Get All Tables
    @GetMapping("/all")
    public List<HotelTable> getAllTables() {
        return service.getAllTables();
    }

    // Get Available Tables
    @GetMapping("/available")
    public List<HotelTable> getAvailableTables() {
        return service.getAvailableTables();
    }

    // Update Table Status
    @PutMapping("/status/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            return ResponseEntity.ok(service.updateStatus(id, status));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Delete Table
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTable(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.deleteTable(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}