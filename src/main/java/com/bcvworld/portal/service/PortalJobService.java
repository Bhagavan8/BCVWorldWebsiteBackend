package com.bcvworld.portal.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bcvworld.portal.exception.ResourceNotFoundException;
import com.bcvworld.portal.model.Job;
import com.bcvworld.portal.model.JobComment;
import com.bcvworld.portal.model.JobLike;
import com.bcvworld.portal.model.JobView;
import com.bcvworld.portal.repository.JobCommentRepository;
import com.bcvworld.portal.repository.JobLikeRepository;
import com.bcvworld.portal.repository.JobRepository;
import com.bcvworld.portal.repository.JobViewRepository;

import jakarta.transaction.Transactional;

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

	public List<Job> getAllJobs() {
		return jobRepository.findAll();
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
}
