package com.bcvworld.portal.controller;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcvworld.portal.model.Suggestion;
import com.bcvworld.portal.repository.SuggestionRepository;

@RestController
@RequestMapping("/api")
public class SuggestionController {

    @Autowired
    private SuggestionRepository suggestionRepository;

 
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
            suggestionRequest.setDate(LocalDateTime.now());

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
