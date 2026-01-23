package com.bcvworld.portal.controller;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bcvworld.portal.model.MentorshipBooking;
import com.bcvworld.portal.model.MentorshipBooking.BookingStatus;
import com.bcvworld.portal.repository.BookingRepository;
import com.bcvworld.portal.service.MentorshipService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/mentorship")
public class MentorshipController {

    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private MentorshipService mentorshipService;

    

    // 2. Book a Session
    @PostMapping("/book")
    public ResponseEntity<?> bookSession(@RequestBody MentorshipBooking booking, HttpServletRequest request) {
        try {
            // Auto-fill system fields
            booking.setTimestamp(LocalDateTime.now());
            booking.setIpAddress(request.getRemoteAddr());
            booking.setStatus(BookingStatus.PENDING_VERIFICATION);
            MentorshipBooking savedBooking = bookingRepository.save(booking);
            
            return ResponseEntity.ok(Map.of(
                "message", "Booking submitted successfully!",
                "bookingId", savedBooking.getId()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<MentorshipBooking>> getAllBookings() {
        return ResponseEntity.ok(mentorshipService.getAllBookings());
    }

    // Approve or Reject Booking
    @PutMapping("/{id}/status")
    public ResponseEntity<MentorshipBooking> updateStatus(
            @PathVariable Long id, 
            @RequestParam String status) {
        return ResponseEntity.ok(mentorshipService.updateStatus(id, status));
    }

    // Reschedule Booking
    @PutMapping("/{id}/reschedule")
    public ResponseEntity<MentorshipBooking> rescheduleBooking(
            @PathVariable Long id, 
            @RequestParam String date, 
            @RequestParam String time) {
        return ResponseEntity.ok(mentorshipService.rescheduleBooking(id, date, time));
    }
}