package com.hotel.system.controller;

import com.hotel.system.entity.Customer;
import com.hotel.system.service.CustomerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
@CrossOrigin("*")
public class CustomerController {

    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    // Add customer
    @PostMapping("/add")
    public Customer addCustomer(@RequestBody Customer c) {
        return service.addCustomer(c);
    }

    // Get all customers
    @GetMapping("/all")
    public List<Customer> getCustomers() {
        return service.getAllCustomers();
    }

    // Get by id
    @GetMapping("/{id}")
    public Customer getCustomer(@PathVariable Long id) {
        return service.getCustomerById(id);
    }

    // Delete customer
    @DeleteMapping("/{id}")
    public String deleteCustomer(@PathVariable Long id) {
        service.deleteCustomer(id);
        return "Customer deleted successfully";
    }
}
