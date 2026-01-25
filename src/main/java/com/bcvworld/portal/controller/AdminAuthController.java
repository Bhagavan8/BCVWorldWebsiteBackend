package com.bcvworld.portal.controller;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bcvworld.portal.JwtUtil;
import com.bcvworld.portal.dto.ApiResponse;
import com.bcvworld.portal.dto.ForgotPasswordRequest;
import com.bcvworld.portal.dto.ResetPasswordRequest;
import com.bcvworld.portal.model.ContactMessage;
import com.bcvworld.portal.model.JobComment;
import com.bcvworld.portal.model.Suggestion;
import com.bcvworld.portal.model.User;
import com.bcvworld.portal.repository.ContactMessageRepository;
import com.bcvworld.portal.repository.JobCommentRepository;
import com.bcvworld.portal.repository.SuggestionRepository;
import com.bcvworld.portal.service.AdminAuthService;
import com.bcvworld.portal.service.PasswordResetService;
import com.bcvworld.portal.service.UserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/api/admin/auth", produces = "application/json")
public class AdminAuthController {

	private final AdminAuthService authService;
	private final JwtUtil jwtUtil;
	private final PasswordResetService passwordResetService;
	private final UserService userService;
    private final ContactMessageRepository messageRepository;
    private final JobCommentRepository commentRepository;
    private final SuggestionRepository suggestionRepository;
    

    public AdminAuthController(
            AdminAuthService authService,
            JwtUtil jwtUtil,
            PasswordResetService passwordResetService,
            UserService userService,
            JobCommentRepository commentRepository,
            ContactMessageRepository messageRepository,
            SuggestionRepository suggestionRepository
    ) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
        this.passwordResetService = passwordResetService;
        this.userService = userService;
        this.commentRepository = commentRepository;
        this.messageRepository = messageRepository;   // ✅ MISSING LINE
        this.suggestionRepository = suggestionRepository;
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
	        return ResponseEntity.badRequest()
	                .body(Map.of("message", "Email and password are required"));
	    }

	    try {
	        User user = authService.login(email, password);

	        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

	        return ResponseEntity.ok(Map.of(
	                "token", token,
	                "role", user.getRole(),
	                "email", user.getEmail(),
	                "name", user.getName(),
	                "createdAT", user.getCreatedAt()
	        ));

	    } catch (IllegalArgumentException e) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                .body(Map.of("message", e.getMessage()));
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
	 @PostMapping("/forgot-password")
	    public ResponseEntity<ApiResponse> forgotPassword(
	            @Valid @RequestBody ForgotPasswordRequest request) {

	        boolean exists = passwordResetService.verifyEmail(request);
	        if (!exists) {
	            ApiResponse body = new ApiResponse(false, "Account not found for this email.");
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
	        }
	        ApiResponse body = new ApiResponse(true, "Email verified. You can now reset your password.");
	        return ResponseEntity.ok(body);
	    }

	    @PostMapping("/reset-password")
	    public ResponseEntity<ApiResponse> resetPassword(
	            @Valid @RequestBody ResetPasswordRequest request) {

	        boolean updated = passwordResetService.resetPassword(request);
	        if (!updated) {
	            ApiResponse body = new ApiResponse(false, "Account not found for this email.");
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
	        }
	        ApiResponse body = new ApiResponse(true, "Password updated successfully.");
	        return ResponseEntity.ok(body);
	    }
	    @GetMapping
	    public ResponseEntity<Page<User>> getAllUsers(
	            @RequestParam(defaultValue = "0") int page,
	            @RequestParam(defaultValue = "10") int size,
	            @RequestParam(required = false) String search,
	            @RequestParam(required = false) String status,
	            @RequestParam(required = false) String role) {
	        
	        Page<User> users = userService.getAllUsers(page, size, search, status, role);
	        return ResponseEntity.ok(users);
	    }

	    @PatchMapping("/{id}/status")
	    public ResponseEntity<?> updateUserStatus(@PathVariable Long id, @RequestBody Map<String, String> payload) {
	        String status = payload.get("status");
	        if (status == null) {
	            return ResponseEntity.badRequest().body("Status is required");
	        }
	        try {
	            User updatedUser = userService.updateUserStatus(id, status);
	            return ResponseEntity.ok(updatedUser);
	        } catch (RuntimeException e) {
	            return ResponseEntity.notFound().build();
	        }
	    }
	    @GetMapping("/contact-messages")
	    public ResponseEntity<Page<ContactMessage>> getMessages(
	            @RequestParam(defaultValue = "0") int page,
	            @RequestParam(defaultValue = "10") int size) {
	        Page<ContactMessage> messages = messageRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(page, size));
	        return ResponseEntity.ok(messages);
	    }

	   

	    // --- Comments ---

	    @GetMapping("/comments")
	    public ResponseEntity<Page<JobComment>> getComments(
	            @RequestParam(defaultValue = "0") int page,
	            @RequestParam(defaultValue = "10") int size,
	            @RequestParam(defaultValue = "") String search) {
	        
	        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
	        
	        if (search == null || search.trim().isEmpty()) {
	            return ResponseEntity.ok(commentRepository.findAll(pageRequest));
	        } else {
	            return ResponseEntity.ok(commentRepository.searchComments(search, pageRequest));
	        }
	    }

	    @DeleteMapping("/comments/{id}")
	    public ResponseEntity<?> deleteComment(@PathVariable Long id) {
	        return commentRepository.findById(id)
	                .map(comment -> {
	                    commentRepository.delete(comment);
	                    return ResponseEntity.ok().build();
	                })
	                .orElse(ResponseEntity.notFound().build());
	    }
	    
	    @GetMapping("/suggestions")
	    public ResponseEntity<Page<Suggestion>> getSuggestions(
	            @RequestParam(defaultValue = "0") int page,
	            @RequestParam(defaultValue = "10") int size) {
	        // Fetch where type is "suggestion"
	        Page<Suggestion> suggestions = suggestionRepository.findAllByOrderByDateDesc(PageRequest.of(page, size));
	        return ResponseEntity.ok(suggestions);
	    }
}
