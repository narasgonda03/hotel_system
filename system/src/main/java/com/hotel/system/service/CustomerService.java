package com.hotel.system.service;

import com.hotel.system.entity.Customer;
import com.hotel.system.entity.Order;
import com.hotel.system.repository.CustomerRepository;
import com.hotel.system.repository.OrderRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository repo;
    private final OrderRepository orderRepo;

    public CustomerService(CustomerRepository repo, OrderRepository orderRepo) {
        this.repo = repo;
        this.orderRepo = orderRepo;
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
    // FIX: Customer ला linked orders असतील तर customer_id NULL करणे
    // Orders history राहील — फक्त customer link तुटेल
    public String deleteCustomer(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Customer not found with id: " + id);
        }

        // Customer च्या orders मध्ये customer_id NULL करणे
        List<Order> linkedOrders = orderRepo.findByCustomerId(id);
        if (!linkedOrders.isEmpty()) {
            linkedOrders.forEach(order -> order.setCustomer(null));
            orderRepo.saveAll(linkedOrders);
        }

        try {
            repo.deleteById(id);
            return linkedOrders.isEmpty()
                    ? "Customer deleted successfully!"
                    : "Customer deleted! " + linkedOrders.size() + " order(s) unlinked (history preserved).";
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Cannot delete customer due to linked records.");
        }
    }
}