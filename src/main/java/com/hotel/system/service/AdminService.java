package com.hotel.system.service;

import com.hotel.system.entity.Admin;
import com.hotel.system.repository.AdminRepository;
import com.hotel.system.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    // FIX #1: PasswordEncoder आणि JwtUtil inject केले — BCrypt + Real JWT साठी
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AdminService(AdminRepository adminRepository,
                        PasswordEncoder passwordEncoder,
                        JwtUtil jwtUtil) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // Register
    public Admin register(Admin admin) {
        if (admin.getRole() == null || admin.getRole().isEmpty()) {
            admin.setRole("ADMIN");
        }
        // FIX #1a: Password BCrypt ने encrypt करणे (आधी plain text save होत होतो)
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        return adminRepository.save(admin);
    }

    // Login — returns { token, role, name, id }
    public Map<String, String> login(Admin admin) {
        Admin existing = adminRepository.findByEmail(admin.getEmail());

        if (existing == null) {
            throw new RuntimeException("Invalid email or password!");
        }
        // FIX #1b: BCrypt password check — आधी plain text equals() होते
        if (!passwordEncoder.matches(admin.getPassword(), existing.getPassword())) {
            throw new RuntimeException("Invalid email or password!");
        }

        // FIX #1c: Real JWT token generate — आधी "token_1_ADMIN" असा fake token होता
        String token = jwtUtil.generateToken(existing.getEmail());

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
            // FIX #1d: Update वेळी पण password encrypt करणे
            existing.setPassword(passwordEncoder.encode(updated.getPassword()));
        }
        return adminRepository.save(existing);
    }

    // Delete staff
    public String deleteAdmin(Long id) {
        adminRepository.deleteById(id);
        return "Staff deleted successfully";
    }
}