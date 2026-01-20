package com.bcvworld.portal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bcvworld.portal.dto.CompanyProjection;
import com.bcvworld.portal.dto.JobResponse;
import com.bcvworld.portal.model.CompanyLogo;
import com.bcvworld.portal.model.Job;
import com.bcvworld.portal.repository.CompanyLogoRepository;
import com.bcvworld.portal.repository.JobRepository;
import com.bcvworld.portal.service.AdminJobService;

@RestController
@RequestMapping("/api/admin/jobs")
public class AdminJobController {

    @Autowired
    private AdminJobService jobService;
    
    @Autowired
    private CompanyLogoRepository companyLogoRepository;
    
    @Autowired
	private JobRepository jobRepository;

    @PostMapping
    public ResponseEntity<Job> createJob(@RequestBody Job job) {
        // Logic to copy Blob from CompanyLogo table to Job table
        if (job.getCompanyLogoId() != null) {
            CompanyLogo logo = companyLogoRepository.findById(job.getCompanyLogoId()).orElse(null);
            if (logo != null) {
                job.setCompanyLogo(logo.getData()); // Copy the blob data
            }
        }

        // Existing save logic
        Job savedJob = jobService.saveJob(job);
        return ResponseEntity.ok(savedJob);
    }
    
    @GetMapping
    public ResponseEntity<List<Job>> getAllJobs() {
        return ResponseEntity.ok(jobService.getAllJobs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Long id) {
        return jobService.getJobById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/management")
    public Page<JobResponse> getJobsForManagement(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "postedDate"));
        return jobService.getJobsForManagement(search, pageable);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/companies/search")
    public ResponseEntity<List<CompanyProjection>> searchCompanies(
            @RequestParam String q) {

        return ResponseEntity.ok(
            jobRepository.findDistinctByCompanyNameContainingIgnoreCase(q)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateJob(
            @PathVariable Long id,
            @RequestBody Job job) {
        return ResponseEntity.ok(jobService.updateJob(id, job));
    }

}
