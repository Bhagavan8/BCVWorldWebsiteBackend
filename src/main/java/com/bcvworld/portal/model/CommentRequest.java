package com.bcvworld.portal.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CommentRequest {

	// Maps to "userId" or "user_id" in JSON
	@JsonProperty("userId")
	private String userId;

	private String userName;
	private String content;
	

	// Default Constructor (REQUIRED)
	public CommentRequest() {
	}

	// Getters and Setters (REQUIRED if not using Lombok @Data)
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
