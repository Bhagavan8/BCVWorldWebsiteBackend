package com.bcvworld.portal.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bcvworld.portal.dto.JobResponse;
import com.bcvworld.portal.model.Job;
import com.bcvworld.portal.repository.JobRepository;



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
		
		if(job.getPostedBy() == null || job.getPostedBy().isBlank()) {
			job.setPostedBy("help.bcvworld@bcvworld.com");
		}
		if(job.getPostedByName() == null || job.getPostedByName().isBlank()) {
			job.setPostedByName("BCVWorld Admin");
		}
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

	  @Transactional(readOnly = true)
	    public Page<JobResponse> getJobsForManagement(String search, Pageable pageable) {

	        Page<Long> jobIdsPage =
	                jobRepository.findJobIdsForManagement(search, pageable);

	        if (jobIdsPage.isEmpty()) {
	            return Page.empty(pageable);
	        }

	        List<Long> ids = jobIdsPage.getContent();

	        Map<Long, Job> jobMap = jobRepository.findJobsWithDetails(ids)
	                .stream()
	                .collect(Collectors.toMap(Job::getId, Function.identity()));

	        List<JobResponse> responses = ids.stream()
	                .map(jobMap::get)
	                .filter(Objects::nonNull)
	                .map(this::mapToJobResponse)
	                .toList();

	        return new PageImpl<>(
	                responses,
	                pageable,
	                jobIdsPage.getTotalElements()
	        );
	    }
	  
	  private JobResponse mapToJobResponse(Job job) {
	        JobResponse dto = new JobResponse();

	        dto.setId(job.getId());
	        dto.setJobId(job.getJobId());
	        dto.setCompanyId(job.getCompanyId());
	        dto.setJobTitle(job.getJobTitle());
	        dto.setCompanyName(job.getCompanyName());
	        dto.setListingStatus(job.getListingStatus());
	        dto.setPostedDate(job.getPostedDate());
	        dto.setViewCount(job.getViewCount());
	        dto.setLikeCount(job.getLikeCount());
	        dto.setIsActive(job.isActive());

	       

	        return dto;
	    }


	@Transactional
	public void deleteJob(Long id) {
	    Job job = jobRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Job not found"));

	    job.setActive(false);   // 👈 Soft delete
	    jobRepository.save(job);
	}
	
	@Transactional
	public Job updateJob(Long id, Job request) {

        Job existing = jobRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Job not found with id: " + id));

        // ===== BASIC JOB INFO =====
        existing.setJobTitle(request.getJobTitle());
        existing.setJobCategory(request.getJobCategory());
        existing.setJobType(request.getJobType());
        existing.setEmploymentType(request.getEmploymentType());

        // ===== COMPANY =====
        existing.setCompanyName(request.getCompanyName());
        existing.setCompanyWebsite(request.getCompanyWebsite());
        existing.setAboutCompany(request.getAboutCompany());
        existing.setUseExistingCompany(request.isUseExistingCompany());
        existing.setCompanyId(request.getCompanyId());
        existing.setLogoUrl(request.getLogoUrl());
        existing.setCompanyLogoId(request.getCompanyLogoId());

        // ⚠️ Only update logo if sent
        if (request.getCompanyLogo() != null && request.getCompanyLogo().length > 0) {
            existing.setCompanyLogo(request.getCompanyLogo());
        }

        // ===== JOB DETAILS =====
        existing.setDescription(request.getDescription());
        existing.setDetails(request.getDetails());
        existing.setSkills(request.getSkills());
        existing.setQualifications(request.getQualifications());
        existing.setWalkinDetails(request.getWalkinDetails());

        existing.setExperienceRequired(request.getExperienceRequired());
        existing.setSalary(request.getSalary());
        existing.setNoticePeriod(request.getNoticePeriod());

        existing.setApplicationMethod(request.getApplicationMethod());
        existing.setApplicationEmail(request.getApplicationEmail());
        existing.setApplicationLink(request.getApplicationLink());

        existing.setListingStatus(request.getListingStatus());
        existing.setActive(request.isActive());
        existing.setReferralCode(request.getReferralCode());

        // ===== DATES =====
        existing.setPostedDate(request.getPostedDate());
        existing.setLastDateToApply(request.getLastDateToApply());

        // ===== ELEMENT COLLECTIONS (IMPORTANT) =====
        existing.getLocations().clear();
        if (request.getLocations() != null) {
            existing.getLocations().addAll(request.getLocations());
        }

        existing.getEducationLevels().clear();
        if (request.getEducationLevels() != null) {
            existing.getEducationLevels().addAll(request.getEducationLevels());
        }

        // ❌ DO NOT TOUCH:
        // viewCount, likeCount, postedBy, postedByName

        return jobRepository.save(existing);
    }
    
}
