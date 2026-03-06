package com.hotel.system.service;

import com.hotel.system.entity.Customer;
import com.hotel.system.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository repo;

    public CustomerService(CustomerRepository repo) {
        this.repo = repo;
    }

    // Add Customer
    public Customer addCustomer(Customer c) {
        return repo.save(c);
    }

    // Get All Customers
    public List<Customer> getAllCustomers() {
        return repo.findAll();
    }

    // Get Customer By ID
    public Customer getCustomerById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
    }

    // Update Customer — नाव, phone, email, address बदलणे
    public Customer updateCustomer(Long id, Customer updatedData) {
        Customer existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));

        // फक्त जे दिलं ते update होईल
        if (updatedData.getName() != null) existing.setName(updatedData.getName());
        if (updatedData.getPhone() != null) existing.setPhone(updatedData.getPhone());
        if (updatedData.getEmail() != null) existing.setEmail(updatedData.getEmail());
        if (updatedData.getAddress() != null) existing.setAddress(updatedData.getAddress());

        return repo.save(existing);
    }

    // Delete Customer
    public void deleteCustomer(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Customer not found with id: " + id);
        }
        repo.deleteById(id);
    }
}