package com.bcvworld.portal.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    private String mobile;
    private String dob;
    private String country;
    private String state;
    private String city;

    private String role; // "EMPLOYEE", "ADMIN"

    // For social login (from Admin)
    private String provider; // "local", "google", "linkedin"
    private String providerId;
    
    private java.time.LocalDateTime createdAt;
    private String ipAddress;
    private String status;
    
    public User() {
    }

    public User(String email, String password, String name, String mobile, String dob, String country, String state, String city, String role, String status) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.mobile = mobile;
        this.dob = dob;
        this.country = country;
        this.state = state;
        this.city = city;
        this.role = role;
        this.status = status;
    }
    
    

	public User(Long id, String email, String password, String name, String mobile, String dob, String country,
			String state, String city, String role, String provider, String providerId, LocalDateTime createdAt,
			String ipAddress, String status) {
		super();
		this.id = id;
		this.email = email;
		this.password = password;
		this.name = name;
		this.mobile = mobile;
		this.dob = dob;
		this.country = country;
		this.state = state;
		this.city = city;
		this.role = role;
		this.provider = provider;
		this.providerId = providerId;
		this.createdAt = createdAt;
		this.ipAddress = ipAddress;
		this.status = status;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	public java.time.LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(java.time.LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
    
}
