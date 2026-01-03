package com.bcvworld.portal.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "job_likes",
    uniqueConstraints = @UniqueConstraint(columnNames = {"job_id", "user_id"})
)
public class JobLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "job_id", nullable = false)
    private Long jobId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    private LocalDateTime likedAt = LocalDateTime.now();

    public JobLike() {}

    public JobLike(Long jobId, String userId) {
        this.jobId = jobId;
        this.userId = userId;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getJobId() {
		return jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public LocalDateTime getLikedAt() {
		return likedAt;
	}

	public void setLikedAt(LocalDateTime likedAt) {
		this.likedAt = likedAt;
	}

    // getters & setters
    
}
