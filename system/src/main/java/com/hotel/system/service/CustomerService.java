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

    public Customer addCustomer(Customer c) {
        return repo.save(c);
    }

    public List<Customer> getAllCustomers() {
        return repo.findAll();
    }

    public Customer getCustomerById(Long id) {
        return repo.findById(id).orElseThrow();
    }

    public void deleteCustomer(Long id) {
        repo.deleteById(id);
    }
}
