package com.bcvworld.portal.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bcvworld.portal.model.MentorshipBooking;
import com.bcvworld.portal.repository.BookingRepository;

@Service
public class MentorshipService {

    @Autowired
    private BookingRepository bookingRepository;

    public List<MentorshipBooking> getAllBookings() {
        return bookingRepository.findAllByOrderByCreatedAtDesc();
    }

    public MentorshipBooking updateStatus(Long id, String status) {
        MentorshipBooking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        
        booking.setStatus(MentorshipBooking.BookingStatus.valueOf(status.toUpperCase()));
        return bookingRepository.save(booking);
    }

    public MentorshipBooking rescheduleBooking(Long id, String newDate, String newTime) {
        MentorshipBooking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        
        booking.setDate(newDate);
        booking.setTimeSlot(newTime);
        booking.setTime(newTime);
        // Optionally reset status to APPROVED or PENDING upon reschedule
        booking.setStatus(MentorshipBooking.BookingStatus.APPROVED); 
        
        return bookingRepository.save(booking);
    }
}
