package com.hotel.system.service;

import com.hotel.system.entity.Admin;
import com.hotel.system.repository.AdminRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {

    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    // Register
    public Admin register(Admin admin) {
        if (admin.getRole() == null || admin.getRole().isEmpty()) {
            admin.setRole("ADMIN");
        }
        return adminRepository.save(admin);
    }

    // Login — returns { token, role, name, id }
    public Map<String, String> login(Admin admin) {
        Admin existing = adminRepository.findByEmail(admin.getEmail());

        if (existing == null) {
            throw new RuntimeException("Email not found!");
        }
        if (!existing.getPassword().equals(admin.getPassword())) {
            throw new RuntimeException("Invalid password!");
        }

        String token = "token_" + existing.getId() + "_" + existing.getRole();

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("role", existing.getRole());
        response.put("name", existing.getName());
        response.put("id", existing.getId().toString());

        return response;
    }

    // Get all staff
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    // Update staff
    public Admin updateAdmin(Long id, Admin updated) {
        Admin existing = adminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Staff not found: " + id));
        existing.setName(updated.getName());
        existing.setEmail(updated.getEmail());
        existing.setRole(updated.getRole());
        if (updated.getPassword() != null && !updated.getPassword().isEmpty()) {
            existing.setPassword(updated.getPassword());
        }
        return adminRepository.save(existing);
    }

    // Delete staff
    public String deleteAdmin(Long id) {
        adminRepository.deleteById(id);
        return "Staff deleted successfully";
    }
}