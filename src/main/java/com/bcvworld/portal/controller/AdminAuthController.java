package com.bcvworld.portal.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcvworld.portal.JwtUtil;
import com.bcvworld.portal.model.User;

import com.bcvworld.portal.service.AdminAuthService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/admin/auth")
public class AdminAuthController {

	private final AdminAuthService authService;
	private final JwtUtil jwtUtil;

	// ✅ Constructor injection (BEST PRACTICE)
	public AdminAuthController(AdminAuthService authService, JwtUtil jwtUtil) {
		this.authService = authService;
		this.jwtUtil = jwtUtil;
	}

	// ================= REGISTER =================
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody User user, HttpServletRequest request) {
		try {
			String ipAddress = request.getHeader("X-Forwarded-For");
			if (ipAddress == null || ipAddress.isEmpty()) {
				ipAddress = request.getRemoteAddr();
			}
			user.setIpAddress(ipAddress);

			User registeredUser = authService.register(user);
			return ResponseEntity.ok(registeredUser);

		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
		}
	}

	// ================= LOGIN =================
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
		try {
			String email = credentials.get("email");
			String password = credentials.get("password");

			User user = authService.login(email, password);

			// ✅ Generate JWT token
			String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

			// ✅ Required response structure
			return ResponseEntity.ok(
					Map.of("token", token, "role", user.getRole(), "email", user.getEmail(), "name", user.getName()));

		} catch (Exception e) {
			return ResponseEntity.status(401).body(Map.of("message", "Invalid email or password"));
		}
	}

	// ================= SOCIAL LOGIN =================
	@PostMapping("/social")
	public ResponseEntity<?> socialLogin(@RequestBody Map<String, String> payload) {
		try {
			String email = payload.get("email");
			String name = payload.get("name");
			String provider = payload.get("provider");

			User user = authService.socialLogin(email, name, provider);

			// Optional: also issue token for social login
			String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

			return ResponseEntity.ok(
					Map.of("token", token, "role", user.getRole(), "email", user.getEmail(), "name", user.getName()));

		} catch (Exception e) {
			return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
		}
	}
}
