package com.bcvworld.portal.model;


import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "mentorship_bookings")
public class MentorshipBooking {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // User Details
    private String name;
    private String email;
    private String phone;
    @Column(columnDefinition = "TEXT")
    private String goal; // Purpose of session

    // Session Details
    private Long sessionId;
    private String date; // YYYY-MM-DD
    private String time; // e.g., "10:00 AM"
    private Double amount;

    // Payment Details
    private String transactionId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

    
    private LocalDateTime timestamp;
    private String ipAddress;
    private LocalDateTime createdAt;
    private String timeSlot;

    public String getTimeSlot() {
		return timeSlot;
	}

	public void setTimeSlot(String timeSlot) {
		this.timeSlot = timeSlot;
	}

	@PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    // Enum for Status
    public enum BookingStatus {
        PENDING_VERIFICATION,
        CONFIRMED,
        REJECTED,
        COMPLETED,
        APPROVED
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getGoal() {
		return goal;
	}

	public void setGoal(String goal) {
		this.goal = goal;
	}

	public Long getSessionId() {
		return sessionId;
	}

	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	

	

	public BookingStatus getStatus() {
		return status;
	}

	public void setStatus(BookingStatus status) {
		this.status = status;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	

	
    
}