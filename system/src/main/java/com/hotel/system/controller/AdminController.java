package com.hotel.system.controller;

import com.hotel.system.entity.Admin;
import com.hotel.system.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class AdminController {

    private final AdminService service;

    public AdminController(AdminService service) {
        this.service = service;
    }

    // Register — Public
    @PostMapping("/register")
    public ResponseEntity<Admin> register(@RequestBody Admin admin) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.register(admin));
    }

    // Login — Public — Token मिळेल
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Admin admin) {
        try {
            String token = service.login(admin);
            // Token + message return करणे
            return ResponseEntity.ok(Map.of(
                    "message", "Login successful",
                    "token", token
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    // सगळे Admins — Protected
    @GetMapping("/all")
    public List<Admin> getAllAdmins() {
        return service.getAllAdmins();
    }

    // Delete Admin — Protected
    @DeleteMapping("/delete/{id}")
    public String deleteAdmin(@PathVariable Long id) {
        return service.deleteAdmin(id);
    }
}