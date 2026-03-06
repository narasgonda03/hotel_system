package com.hotel.system.service;

import com.hotel.system.entity.User;
import com.hotel.system.repository.UserRepository;
import com.hotel.system.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // User Register — Admin करेल
    public User register(User user) {
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("WAITER");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // Login — Token + Role + Name return
    public Map<String, String> login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!user.isActive()) {
            throw new RuntimeException("Account is disabled! Contact admin.");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(email);

        return Map.of(
                "token", token,
                "role", user.getRole(),
                "name", user.getName(),
                "userId", user.getId().toString(),
                "message", "Login successful"
        );
    }

    // सगळे Users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Role नुसार Users
    public List<User> getUsersByRole(String role) {
        return userRepository.findByRole(role);
    }

    // User Update
    public User updateUser(Long id, User updatedData) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
        if (updatedData.getName() != null) user.setName(updatedData.getName());
        if (updatedData.getRole() != null) user.setRole(updatedData.getRole());
        if (updatedData.getPassword() != null && !updatedData.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(updatedData.getPassword()));
        }
        return userRepository.save(user);
    }

    // Enable / Disable User
    public String toggleStatus(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
        user.setActive(!user.isActive());
        userRepository.save(user);
        return user.isActive() ? "User enabled" : "User disabled";
    }

    // Delete User
    public String deleteUser(Long id) {
        userRepository.deleteById(id);
        return "User deleted";
    }
}