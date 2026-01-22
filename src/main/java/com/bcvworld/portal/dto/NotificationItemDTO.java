package com.bcvworld.portal.dto;



import java.time.LocalDateTime;


public class NotificationItemDTO {
    private String id;          // Entity ID
    private String type;        // "USER", "COMMENT", "SUGGESTION"
    private String title;       // e.g., "NEW USER"
    private String description; // e.g., "email@example.com joined"
    private String link;        // Frontend route
    private LocalDateTime createdAt;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public NotificationItemDTO(String id, String type, String title, String description, String link,
			LocalDateTime createdAt) {
		super();
		this.id = id;
		this.type = type;
		this.title = title;
		this.description = description;
		this.link = link;
		this.createdAt = createdAt;
	}

	
	
}