package com.bcvworld.portal.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bcvworld.portal.model.User;
import com.bcvworld.portal.repository.UserRepository;

@Service
public class AdminAuthService {

    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    AdminAuthService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

 
    public User register(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already in use");
        }
        user.setProvider("local");
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("EMPLOYEE");
        }
        
        user.setCreatedAt(java.time.LocalDateTime.now());
        
        // Hash the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        return userRepository.save(user);
    }

    public User login(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Check password using BCrypt
            if (passwordEncoder.matches(password, user.getPassword())) {
                return user;
            }
        }
        throw new RuntimeException("Invalid credentials");
    }

    public User socialLogin(String email, String name, String provider) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            return userOpt.get();
        } else {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setName(name);
            newUser.setProvider(provider);
            newUser.setPassword(""); // No password for social users
            newUser.setRole("EMPLOYEE");
            return userRepository.save(newUser);
        }
    }
}
