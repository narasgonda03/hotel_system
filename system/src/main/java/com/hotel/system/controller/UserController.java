package com.hotel.system.controller;

import com.hotel.system.entity.User;
import com.hotel.system.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@CrossOrigin("*")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    // Register — Admin only
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.register(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Login — सगळे roles
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        try {
            return ResponseEntity.ok(service.login(
                    credentials.get("email"),
                    credentials.get("password")
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    // सगळे Users
    @GetMapping("/all")
    public List<User> getAllUsers() {
        return service.getAllUsers();
    }

    // Role नुसार Users
    @GetMapping("/role/{role}")
    public List<User> getByRole(@PathVariable String role) {
        return service.getUsersByRole(role);
    }

    // Update
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody User user) {
        try {
            return ResponseEntity.ok(service.updateUser(id, user));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Enable / Disable
    @PutMapping("/toggle/{id}")
    public ResponseEntity<String> toggle(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.toggleStatus(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Delete
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.deleteUser(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}