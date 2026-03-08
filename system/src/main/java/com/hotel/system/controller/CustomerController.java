package com.hotel.system.controller;

import com.hotel.system.entity.Customer;
import com.hotel.system.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/customers")
// FIX #5: @CrossOrigin("*") काढला — CorsConfig.java globally handle करतो
public class CustomerController {

    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    // Add Customer
    @PostMapping("/add")
    public ResponseEntity<Customer> addCustomer(@RequestBody Customer c) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addCustomer(c));
    }

    // Get All Customers
    @GetMapping("/all")
    public List<Customer> getCustomers() {
        return service.getAllCustomers();
    }

    // Get By ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomer(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.getCustomerById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Update Customer
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable Long id,
                                            @RequestBody Customer customer) {
        try {
            return ResponseEntity.ok(service.updateCustomer(id, customer));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Delete Customer
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long id) {
        try {
            service.deleteCustomer(id);
            return ResponseEntity.ok("Customer deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}