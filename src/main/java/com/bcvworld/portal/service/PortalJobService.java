package com.bcvworld.portal.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bcvworld.portal.dto.JobDetailResponse;
import com.bcvworld.portal.dto.JobResponse;
import com.bcvworld.portal.exception.ResourceNotFoundException;
import com.bcvworld.portal.model.Job;
import com.bcvworld.portal.model.JobComment;
import com.bcvworld.portal.model.JobLike;
import com.bcvworld.portal.model.JobView;
import com.bcvworld.portal.repository.JobCommentRepository;
import com.bcvworld.portal.repository.JobLikeRepository;
import com.bcvworld.portal.repository.JobRepository;
import com.bcvworld.portal.repository.JobViewRepository;



@Service
public class PortalJobService {

	private final JobRepository jobRepository;
	private final JobViewRepository jobViewRepository;
	private final JobLikeRepository jobLikeRepository;
	private JobCommentRepository jobCommentRepository;

	// ✅ MANUAL CONSTRUCTOR (BEST PRACTICE)
	public PortalJobService(JobRepository jobRepository, JobViewRepository jobViewRepository,
			JobLikeRepository jobLikeRepository,JobCommentRepository jobCommentRepository ) {
		this.jobRepository = jobRepository;
		this.jobViewRepository = jobViewRepository;
		this.jobLikeRepository = jobLikeRepository;
		this.jobCommentRepository = jobCommentRepository;
	}

	@Transactional(readOnly = true)
	public List<JobResponse> getAllJobs() {

	    return jobRepository.findAllWithLocationsAndEducation()
	            .stream()
	            .map(job -> {
	                JobResponse dto = new JobResponse();

	                dto.setId(job.getId());
	                dto.setJobId(job.getJobId());
	                dto.setJobTitle(job.getJobTitle());
	                dto.setJobCategory(job.getJobCategory());
	                dto.setJobType(job.getJobType());
	                dto.setCompanyName(job.getCompanyName());
	                dto.setSalary(job.getSalary());
	                dto.setViewCount(job.getViewCount());
	                dto.setLikeCount(job.getLikeCount());

	                // ✅ CONVERT Set → List
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

	                dto.setPostedDate(job.getPostedDate());
	                dto.setLastDateToApply(job.getLastDateToApply());

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

	// 🔹 LIKE / UNLIKE
	public Job toggleLike(Long jobId, String userId) {
		Job job = getJobById(jobId);

		return jobLikeRepository.findByJobIdAndUserId(jobId, userId).map(like -> {
			jobLikeRepository.delete(like);
			job.setLikeCount(Math.max(0, job.getLikeCount() - 1));
			job.setLiked(false);
			return jobRepository.save(job);
		}).orElseGet(() -> {
			jobLikeRepository.save(new JobLike(jobId, userId));
			job.setLikeCount(job.getLikeCount() + 1);
			job.setLiked(true);
			return jobRepository.save(job);
		});
	}
	

	public JobComment addComment(Long jobId, Long userId, String userName, String content) {
	    JobComment comment = new JobComment();
	    comment.setJobId(jobId);
	    comment.setUserId(userId);
	    comment.setUserName(userName);
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

	    dto.setId(job.getId());
	    dto.setJobId(job.getJobId());
	    dto.setJobTitle(job.getJobTitle());
	    dto.setCompanyName(job.getCompanyName());
	    dto.setDescription(job.getDescription());
	    dto.setSalary(job.getSalary());
	    dto.setViewCount(job.getViewCount());
	    dto.setLikeCount(job.getLikeCount());

	    dto.setLocations(new ArrayList<>(job.getLocations()));
	    dto.setEducationLevels(new ArrayList<>(job.getEducationLevels()));

	    return dto;
	}

}
