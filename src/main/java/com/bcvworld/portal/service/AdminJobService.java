package com.bcvworld.portal.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bcvworld.portal.model.Job;
import com.bcvworld.portal.repository.JobRepository;

import jakarta.transaction.Transactional;

@Service
public class AdminJobService {

	@Autowired
	private JobRepository jobRepository;

	public Job saveJob(Job job) {

		// Generate Job ID
		if (job.getJobId() == null || job.getJobId().isBlank()) {
			job.setJobId(generateUniqueJobId());
		}

		// Posted date
		if (job.getPostedDate() == null) {
			job.setPostedDate(LocalDate.now());
		}

		// Safety default
		if (job.getExperienceRequired() == null || job.getExperienceRequired().isBlank()) {
			job.setExperienceRequired("FRESHER");
		}

		job.setActive(true);

		// Validate application method
		boolean hasLink = job.getApplicationLink() != null && !job.getApplicationLink().isBlank();

		boolean hasEmail = job.getApplicationEmail() != null && !job.getApplicationEmail().isBlank();

		if (!hasLink && !hasEmail) {
			throw new RuntimeException("Application link or email is required");
		}
		job.setActive(true);
		return jobRepository.save(job);
	}

	public List<Job> getAllJobs() {
		return jobRepository.findAll();
	}

	public Optional<Job> getJobById(Long id) {
		return jobRepository.findById(id);
	}

	private String generateUniqueJobId() {
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

		for (int i = 0; i < 1000; i++) {
			StringBuilder sb = new StringBuilder(10);
			for (int j = 0; j < 10; j++) {
				sb.append(chars.charAt(ThreadLocalRandom.current().nextInt(chars.length())));
			}
			String id = sb.toString();
			if (!jobRepository.existsByJobId(id)) {
				return id;
			}
		}
		throw new RuntimeException("Unable to generate unique job ID");
	}

	public Page<Job> getJobsForManagement(String search, Pageable pageable) {
		if (search != null && !search.trim().isEmpty()) {
			return jobRepository.findByJobTitleContainingIgnoreCaseOrCompanyNameContainingIgnoreCase(search, search,
					pageable);
		}
		return jobRepository.findAll(pageable);
	}

	@Transactional
	public void deleteJob(Long id) {
	    Job job = jobRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Job not found"));

	    job.setActive(false);   // 👈 Soft delete
	    jobRepository.save(job);
	}
}
