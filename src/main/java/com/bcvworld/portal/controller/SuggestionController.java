package com.bcvworld.backend.controller;

import com.bcvworld.backend.model.Suggestion;
import com.bcvworld.backend.repository.SuggestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Allow requests from any origin (e.g., your frontend)
public class SuggestionController {

    @Autowired
    private SuggestionRepository suggestionRepository;

    @GetMapping("/")
    public String healthCheck() {
        return "BCVWorld Backend is Running";
    }

    /**
     * POST /api/suggestion
     * Receives suggestion form data from the frontend and saves to MySQL
     */
    @PostMapping("/suggestion")
    public Map<String, Object> createSuggestion(@RequestBody Suggestion suggestionRequest) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Basic Validation
            if (!suggestionRequest.isAgreed()) {
                response.put("success", false);
                response.put("message", "You must agree to the terms.");
                return response;
            }

            // Set current date
            suggestionRequest.setDate(LocalDateTime.now().toString());

            // Save to MySQL Database
            Suggestion savedSuggestion = suggestionRepository.save(suggestionRequest);

            // Log to console
            System.out.println("✅ New Suggestion Saved to Database:");
            System.out.println(savedSuggestion);

            // Response
            response.put("success", true);
            response.put("message", "Suggestion submitted successfully!");
            response.put("data", savedSuggestion);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Internal Server Error: " + e.getMessage());
        }
        
        return response;
    }
}
