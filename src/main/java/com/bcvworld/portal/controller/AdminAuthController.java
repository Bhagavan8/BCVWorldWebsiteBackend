package com.bcvworld.portal.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcvworld.portal.JwtUtil;
import com.bcvworld.portal.model.User;
import com.bcvworld.portal.service.AdminAuthService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api/admin/auth", produces = "application/json")
public class AdminAuthController {

	private final AdminAuthService authService;
	private final JwtUtil jwtUtil;

	public AdminAuthController(AdminAuthService authService, JwtUtil jwtUtil) {
		this.authService = authService;
		this.jwtUtil = jwtUtil;
	}

	// ================= REGISTER =================
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody User user, HttpServletRequest request) {

		try {
			String ipAddress = request.getHeader("X-Forwarded-For");
			if (ipAddress == null || ipAddress.isBlank()) {
				ipAddress = request.getRemoteAddr();
			}
			user.setIpAddress(ipAddress);

			User registeredUser = authService.register(user);

			return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);

		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
		}
	}

	// ================= LOGIN =================
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {

		String email = credentials.get("email");
		String password = credentials.get("password");

		if (email == null || password == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Map.of("message", "Email and password are required"));
		}

		try {
			User user = authService.login(email, password);

			String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

			return ResponseEntity.ok(
					Map.of("token", token, "role", user.getRole(), "email", user.getEmail(), "name", user.getName(), "createdAT", user.getCreatedAt()));

		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid email or password"));
		}
	}

	// ================= SOCIAL LOGIN =================
	@PostMapping("/social")
	public ResponseEntity<?> socialLogin(@RequestBody Map<String, String> payload) {
	    String provider = payload.get("provider");
	    String idToken = payload.get("idToken");
	    if (provider == null || idToken == null) {
	        return ResponseEntity.badRequest().body(Map.of("message", "Invalid social login payload"));
	    }

	    GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
	            .setAudience(List.of("1083295808227-qso07ckp6d725qlq53j7n9satpe9grgm.apps.googleusercontent.com"))
	            .build();

	    try {
	        GoogleIdToken token = verifier.verify(idToken);
	        if (token == null) {
	            return ResponseEntity.badRequest().body(Map.of("message", "Invalid Google token"));
	        }
	        GoogleIdToken.Payload p = token.getPayload();
	        String email = p.getEmail();
	        String name = (String) p.get("name");
	        String providerId = p.getSubject();

	        User user = authService.upsertSocialUser(email, name, provider, providerId);
	        String jwt = jwtUtil.generateToken(user.getEmail(), user.getRole());
	        return ResponseEntity.ok(Map.of("token", jwt, "role", user.getRole(), "email", user.getEmail(), "name", user.getName()));
	    } catch (Exception e) {
	        return ResponseEntity.badRequest().body(Map.of("message", "Social login failed"));
	    }
	}
}
