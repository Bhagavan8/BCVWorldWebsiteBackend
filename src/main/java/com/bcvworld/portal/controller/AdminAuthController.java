package com.bcvworld.portal.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bcvworld.portal.JwtUtil;
import com.bcvworld.portal.model.User;
import com.bcvworld.portal.service.AdminAuthService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(
        value = "/api/admin/auth",
        produces = "application/json"
)
public class AdminAuthController {

    private final AdminAuthService authService;
    private final JwtUtil jwtUtil;

    public AdminAuthController(AdminAuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    // ================= REGISTER =================
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody User user,
            HttpServletRequest request
    ) {

        try {
            String ipAddress = request.getHeader("X-Forwarded-For");
            if (ipAddress == null || ipAddress.isBlank()) {
                ipAddress = request.getRemoteAddr();
            }
            user.setIpAddress(ipAddress);

            User registeredUser = authService.register(user);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(registeredUser);

        } catch (RuntimeException e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // ================= LOGIN =================
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody Map<String, String> credentials
    ) {

        String email = credentials.get("email");
        String password = credentials.get("password");

        if (email == null || password == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Email and password are required"));
        }

        try {
            User user = authService.login(email, password);

            String token = jwtUtil.generateToken(
                    user.getEmail(),
                    user.getRole()
            );

            return ResponseEntity.ok(
                    Map.of(
                            "token", token,
                            "role", user.getRole(),
                            "email", user.getEmail(),
                            "name", user.getName()
                    )
            );

        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid email or password"));
        }
    }

    // ================= SOCIAL LOGIN =================
    @PostMapping("/social")
    public ResponseEntity<?> socialLogin(
            @RequestBody Map<String, String> payload
    ) {

        try {
            String email = payload.get("email");
            String name = payload.get("name");
            String provider = payload.get("provider");

            if (email == null || provider == null) {
                return ResponseEntity
                        .badRequest()
                        .body(Map.of("message", "Invalid social login payload"));
            }

            User user = authService.socialLogin(email, name, provider);

            String token = jwtUtil.generateToken(
                    user.getEmail(),
                    user.getRole()
            );

            return ResponseEntity.ok(
                    Map.of(
                            "token", token,
                            "role", user.getRole(),
                            "email", user.getEmail(),
                            "name", user.getName()
                    )
            );

        } catch (RuntimeException e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("message", e.getMessage()));
        }
    }
}
