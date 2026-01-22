package com.bcvworld.portal.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bcvworld.portal.dto.JobDetailResponse;
import com.bcvworld.portal.dto.JobLikeResponse;
import com.bcvworld.portal.dto.JobResponse;
import com.bcvworld.portal.exception.ResourceNotFoundException;
import com.bcvworld.portal.model.Job;
import com.bcvworld.portal.model.JobComment;
import com.bcvworld.portal.model.JobLike;
import com.bcvworld.portal.model.JobView;
import com.bcvworld.portal.model.User;
import com.bcvworld.portal.repository.JobCommentRepository;
import com.bcvworld.portal.repository.JobLikeRepository;
import com.bcvworld.portal.repository.JobRepository;
import com.bcvworld.portal.repository.JobViewRepository;
import com.bcvworld.portal.repository.UserRepository;



@Service
public class PortalJobService {

	private final JobRepository jobRepository;
	private final JobViewRepository jobViewRepository;
	private final JobLikeRepository jobLikeRepository;
	private JobCommentRepository jobCommentRepository;
	private final UserRepository userRepository;

	// ✅ MANUAL CONSTRUCTOR (BEST PRACTICE)
	public PortalJobService(JobRepository jobRepository, JobViewRepository jobViewRepository,
			JobLikeRepository jobLikeRepository,JobCommentRepository jobCommentRepository,UserRepository userRepository) {
		this.jobRepository = jobRepository;
		this.jobViewRepository = jobViewRepository;
		this.jobLikeRepository = jobLikeRepository;
		this.jobCommentRepository = jobCommentRepository;
		this.userRepository = userRepository;
	}

	@Transactional(readOnly = true)
	public List<JobResponse> getAllJobs() {

	    return jobRepository.findAllWithLocationsAndEducation()
	            .stream()
	            .map(job -> {
	                JobResponse dto = new JobResponse();

	                // ========================
	                // IDENTIFIERS
	                // ========================
	                dto.setId(job.getId());
	                dto.setJobId(job.getJobId());
	                dto.setCompanyId(job.getCompanyId());

	                // ========================
	                // JOB BASIC DETAILS
	                // ========================
	                dto.setJobTitle(job.getJobTitle());
	                dto.setJobCategory(job.getJobCategory());
	                dto.setJobType(job.getJobType());
	                dto.setEmploymentType(job.getEmploymentType());
	                dto.setExperienceRequired(job.getExperienceRequired());
	                dto.setNoticePeriod(job.getNoticePeriod());
	                dto.setSalaryRange(job.getSalary());
	                dto.setPostedBy(job.getPostedBy());
	        	    dto.setPostedByName(job.getPostedByName());

	                // ========================
	                // COMPANY DETAILS
	                // ========================
	                dto.setCompanyName(job.getCompanyName());
	                dto.setCompanyWebsite(job.getCompanyWebsite());
	                dto.setAboutCompany(job.getAboutCompany());
	                dto.setCompanyLogoUrl(job.getLogoUrl());

	                // ========================
	                // JOB CONTENT
	                // ========================
	                dto.setDescription(job.getDescription());
	                dto.setDetails(job.getDetails());
	                dto.setQualifications(job.getQualifications());
	                dto.setSkills(job.getSkills());
	                dto.setWalkinDetails(job.getWalkinDetails());

	                // ========================
	                // APPLICATION DETAILS
	                // ========================
	                dto.setApplicationMethod(job.getApplicationMethod());
	                dto.setApplicationEmail(job.getApplicationEmail());
	                dto.setApplicationLink(job.getApplicationLink());
	                dto.setReferralCode(job.getReferralCode());

	                // ========================
	                // STATUS & META
	                // ========================
	                dto.setIsActive(job.isActive());
	                dto.setListingStatus(job.getListingStatus());
	                dto.setPostedDate(job.getPostedDate());
	                dto.setLastDateToApply(job.getLastDateToApply());

	                // ========================
	                // COUNTS
	                // ========================
	                dto.setViewCount(job.getViewCount());
	                dto.setLikeCount(job.getLikeCount());

	                // ========================
	                // COLLECTIONS (Set → List)
	                // ========================
	                dto.setLocations(
	                        job.getLocations() == null
	                                ? List.of()
	                                : new ArrayList<>(job.getLocations())
	                );

	                dto.setEducationLevels(
	                        job.getEducationLevels() == null
	                                ? List.of()
	                                : new ArrayList<>(job.getEducationLevels())
	                );

	                return dto;
	            })
	            .toList();
	}




	public Job getJobById(Long jobId) {
		return jobRepository.findById(jobId).orElseThrow(() -> new ResourceNotFoundException("Job not found"));
	}

	@Transactional
	public void incrementViewCount(Long jobId, String userId) {

	    Job job = jobRepository.findById(jobId)
	            .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

	    boolean isLoggedIn = userId != null && !userId.isBlank();

	    if (isLoggedIn) {

	        boolean alreadyViewed =
	                jobViewRepository.existsByJobIdAndUserId(jobId, userId);

	        if (alreadyViewed) {
	            return; // already viewed → do nothing
	        }
	    }

	    // ✅ Increment view count
	    job.setViewCount(job.getViewCount() + 1);
	    jobRepository.save(job);

	    // ✅ ALWAYS save JobView
	    JobView view = new JobView();
	    view.setJobId(jobId);
	    view.setUserId(isLoggedIn ? userId : null); // 👈 NULL for anonymous
	    view.setViewedAt(LocalDateTime.now());

	    jobViewRepository.save(view);
	}

	@Transactional
	public JobLikeResponse toggleLike(Long jobId, String userId) {

	    Job job = getJobById(jobId);

	    // ✅ Null-safe initialization
	    int likeCount = job.getLikeCount() == null ? 0 : job.getLikeCount();

	    boolean liked;

	    Optional<JobLike> existingLike =
	            jobLikeRepository.findByJobIdAndUserId(jobId, userId);

	    if (existingLike.isPresent()) {
	        jobLikeRepository.delete(existingLike.get());
	        likeCount = Math.max(0, likeCount - 1);
	        liked = false;
	    } else {
	        jobLikeRepository.save(new JobLike(jobId, userId));
	        likeCount = likeCount + 1;
	        liked = true;
	    }

	    job.setLikeCount(likeCount);
	    jobRepository.save(job);

	    JobLikeResponse response = new JobLikeResponse();
	    response.setJobId(jobId);
	    response.setLikeCount(likeCount);
	    response.setLiked(liked);

	    return response;
	}


	

	public JobComment addComment(Long jobId, String userEmail, String userName, String content) {

	    User user = userRepository.findByEmail(userEmail)
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    JobComment comment = new JobComment();
	    comment.setJobId(jobId);
	    comment.setUserId(user.getId());
	    comment.setUserName(userName);
	    comment.setUserEmail(userEmail);
	    comment.setContent(content);

	    return jobCommentRepository.save(comment);
	}


	public List<JobComment> getJobComments(Long jobId) {
	    return jobCommentRepository.findByJobIdOrderByCreatedAtDesc(jobId);
	}
	@Transactional(readOnly = true)
	public JobDetailResponse getJobDetails(Long id) {

	    Job job = jobRepository.findByIdWithDetails(id)
	            .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

	    JobDetailResponse dto = new JobDetailResponse();

	    // ========================
	    // IDENTIFIERS
	    // ========================
	    dto.setId(job.getId());
	    dto.setJobId(job.getJobId());
	    dto.setCompanyId(job.getCompanyId());

	    // ========================
	    // JOB BASIC DETAILS
	    // ========================
	    dto.setJobTitle(job.getJobTitle());
	    dto.setJobCategory(job.getJobCategory());
	    dto.setJobType(job.getJobType());
	    dto.setEmploymentType(job.getEmploymentType());
	    dto.setExperienceRequired(job.getExperienceRequired());
	    dto.setNoticePeriod(job.getNoticePeriod());
	    dto.setSalary(job.getSalary());
	    dto.setPostedBy(job.getPostedBy());
	    dto.setPostedByName(job.getPostedByName());

	    // ========================
	    // COMPANY DETAILS
	    // ========================
	    dto.setCompanyName(job.getCompanyName());
	    dto.setCompanyWebsite(job.getCompanyWebsite());
	    dto.setCompanyLogoUrl(job.getLogoUrl());
	    dto.setUseExistingCompany(job.isUseExistingCompany());
	    dto.setAboutCompany(job.getAboutCompany());

	    // ========================
	    // JOB CONTENT
	    // ========================
	    dto.setDescription(job.getDescription());
	    dto.setDetails(job.getDetails());
	    dto.setSkills(job.getSkills());
	    dto.setQualifications(job.getQualifications());
	    dto.setWalkinDetails(job.getWalkinDetails());

	    // ========================
	    // APPLICATION DETAILS
	    // ========================
	    dto.setApplicationMethod(job.getApplicationMethod());
	    dto.setApplicationEmail(job.getApplicationEmail());
	    dto.setApplicationLink(job.getApplicationLink());
	    dto.setReferralCode(job.getReferralCode());

	    // ========================
	    // STATUS & META
	    // ========================
	    dto.setListingStatus(job.getListingStatus());
	    dto.setActive(job.isActive());
	    dto.setPostedDate(job.getPostedDate());
	    dto.setLastDateToApply(job.getLastDateToApply());

	    // ========================
	    // COUNTS
	    // ========================
	    dto.setViewCount(job.getViewCount());
	    dto.setLikeCount(job.getLikeCount());
	    dto.setLiked(job.isLiked());

	    // ========================
	    // COLLECTIONS (Set → List)
	    // ========================
	    dto.setLocations(
	            job.getLocations() == null
	                    ? List.of()
	                    : new ArrayList<>(job.getLocations())
	    );

	    dto.setEducationLevels(
	            job.getEducationLevels() == null
	                    ? List.of()
	                    : new ArrayList<>(job.getEducationLevels())
	    );

	    return dto;
	}


}
