package com.hotel.system.service;

import com.hotel.system.entity.Admin;
import com.hotel.system.repository.AdminRepository;
import com.hotel.system.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder; // BCrypt

    public AdminService(AdminRepository adminRepository,
                        JwtUtil jwtUtil,
                        PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    // Admin Register — Password BCrypt encrypt होईल
    public Admin register(Admin admin) {
        // Plain password → BCrypt encrypted password
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        return adminRepository.save(admin);
    }

    // Admin Login — BCrypt verify होईल
    public String login(Admin admin) {
        Admin existing = adminRepository.findByEmail(admin.getEmail());

        if (existing == null) {
            throw new RuntimeException("Invalid email or password");
        }

        // BCrypt password match check
        if (!passwordEncoder.matches(admin.getPassword(), existing.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        // Token generate करणे
        return jwtUtil.generateToken(existing.getEmail());
    }

    // सगळे Admins
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    // Admin Delete
    public String deleteAdmin(Long id) {
        adminRepository.deleteById(id);
        return "Admin deleted successfully";
    }
}