package com.bcvworld.portal.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bcvworld.portal.model.User;
import com.bcvworld.portal.repository.UserRepository;

@Service
public class AdminAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminAuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // =========================
    // REGISTER (LOCAL USERS)
    // =========================
    public User register(User user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new RuntimeException("Password is required");
        }

        user.setProvider("local");
        user.setRole(
                user.getRole() == null || user.getRole().isBlank()
                        ? "ADMIN"
                        : user.getRole()
        );

        user.setCreatedAt(LocalDateTime.now());

        // ✅ ALWAYS BCrypt
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    // =========================
    // LOGIN (LOCAL USERS ONLY)
    // =========================
    public User login(String email, String password) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        // ❗ Block social users from password login
        if (!"local".equals(user.getProvider())) {
            throw new RuntimeException("Use social login");
        }

        // ❗ Ensure password is BCrypt
        if (user.getPassword() == null || !user.getPassword().startsWith("$2a$")) {
            throw new RuntimeException("Password not set");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return user;
    }

    // =========================
    // SOCIAL LOGIN
    // =========================
    public User socialLogin(String email, String name, String provider) {

        return userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User u = new User();
                    u.setEmail(email);
                    u.setName(name);
                    u.setProvider(provider);
                    u.setPassword(null); // ✅ IMPORTANT FIX
                    u.setRole("EMPLOYEE");
                    u.setCreatedAt(LocalDateTime.now());
                    return userRepository.save(u);
                });
    }
}
