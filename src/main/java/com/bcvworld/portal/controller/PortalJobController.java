package com.bcvworld.portal.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bcvworld.portal.dto.JobDetailResponse;
import com.bcvworld.portal.dto.JobResponse;
import com.bcvworld.portal.model.CommentRequest;
import com.bcvworld.portal.model.Job;
import com.bcvworld.portal.model.JobComment;
import com.bcvworld.portal.service.PortalJobService;

@RestController
@RequestMapping("/api/jobs")
public class PortalJobController {

    private static final Logger logger =
            LoggerFactory.getLogger(PortalJobController.class);

    private final PortalJobService jobService;
    public PortalJobController(PortalJobService jobService) {
        this.jobService = jobService;
    }

  
    @GetMapping
    public ResponseEntity<List<JobResponse>> getAllJobs() {
        logger.info("Fetching all jobs");
        return ResponseEntity.ok(jobService.getAllJobs());
    }


    // 🔹 GET JOB BY ID
    @GetMapping("/{id}")
    public ResponseEntity<JobDetailResponse> getJobById(@PathVariable Long id) {
        return ResponseEntity.ok(jobService.getJobDetails(id));
    }


    // 🔹 INCREMENT VIEW COUNT
    // userId is optional (guest users allowed)
    @PostMapping("/{id}/view")
    public ResponseEntity<Void> incrementViewCount(
            @PathVariable Long id,
            @RequestParam(required = false) String userId) {

        jobService.incrementViewCount(id, userId);
        return ResponseEntity.ok().build();
    }

    // 🔹 LIKE / UNLIKE
    @PostMapping("/{id}/like")
    public ResponseEntity<Job> toggleLike(
            @PathVariable Long id,
            @RequestParam String userId) {

        Job updatedJob = jobService.toggleLike(id, userId);
        return ResponseEntity.ok(updatedJob);
    }


    @GetMapping("/{id}/comments")
    public ResponseEntity<List<JobComment>> getComments(@PathVariable Long id) {
        return ResponseEntity.ok(jobService.getJobComments(id));
    }

 // In JobController.java

    @PostMapping("/{id}/comments")
    public ResponseEntity<JobComment> addComment(@PathVariable Long id, @RequestBody CommentRequest request) {
        // Debugging: Print received values
        System.out.println("Received Comment Request: " + request);
        if (request.getUserId() == null) {
            System.out.println("ERROR: UserId is null in request");
            // Handle this case, maybe return bad request or throw exception
            throw new IllegalArgumentException("User ID cannot be null");
        }

        JobComment comment = jobService.addComment(id, request.getUserId(), request.getUserName(), request.getContent());
        return ResponseEntity.ok(comment);
    }
}
