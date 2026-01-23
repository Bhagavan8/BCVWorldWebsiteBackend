package com.bcvworld.portal.dto;



import lombok.Data;

@Data
public class BookingRequest {
    private String name;
    private String email;
    private String phone;
    private String goal; // Maps to 'purpose'
    private String sessionId;
    private String sessionTitle;
    private Double amount;
    private String date; // ISO Date String
    private String time;
    private String transactionId;
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
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getSessionTitle() {
		return sessionTitle;
	}
	public void setSessionTitle(String sessionTitle) {
		this.sessionTitle = sessionTitle;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
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
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
    
}