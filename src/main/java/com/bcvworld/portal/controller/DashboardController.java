package com.bcvworld.portal.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcvworld.portal.repository.JobCommentRepository;
import com.bcvworld.portal.repository.JobRepository;
import com.bcvworld.portal.repository.JobViewRepository;
import com.bcvworld.portal.repository.SuggestionRepository;
import com.bcvworld.portal.repository.UserRepository;

@RestController
@RequestMapping("/api/admin")
public class DashboardController {

	private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

	@Autowired
	private JobRepository jobRepository;
	@Autowired
	private JobViewRepository jobViewRepository;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JobCommentRepository jobCommentRepository;

	@Autowired
	private SuggestionRepository suggestionRepository;


	@GetMapping("/stats")
	public ResponseEntity<Map<String, Object>> getStats() {
		logger.info("getStats called");
		try {
			Map<String, Object> stats = new HashMap<>();

			logger.info("Counting total jobs...");
			long totalJobs = jobRepository.count();
			logger.info("Total jobs: {}", totalJobs);

			logger.info("Counting today jobs...");
			long todayJobs = 0;
			try {
				todayJobs = jobRepository.countByPostedDate(LocalDate.now());
			} catch (Exception e) {
				logger.error("Error counting today jobs: ", e);
			}

			logger.info("Counting yesterday jobs...");
			long yesterdayJobs = 0;
			try {
				yesterdayJobs = jobRepository.countByPostedDate(LocalDate.now().minusDays(1));
			} catch (Exception e) {
				logger.error("Error counting yesterday jobs: ", e);
			}

			logger.info("Counting weekly jobs...");
			long weeklyJobs = 0;
			try {
				weeklyJobs = jobRepository.countByPostedDateBetween(LocalDate.now().minusDays(6), LocalDate.now());
			} catch (Exception e) {
				logger.error("Error counting weekly jobs: ", e);
			}

			logger.info("Counting active users...");
			long activeUsers = userRepository.count();
			logger.info("Active users: {}", activeUsers);

			// Graph Data Calculation
			List<String> months = new ArrayList<>();
			List<Long> privateJobs = new ArrayList<>();
			List<Long> govtJobs = new ArrayList<>();
			List<Long> bankJobs = new ArrayList<>();

			LocalDate now = LocalDate.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM");

			for (int i = 5; i >= 0; i--) {
				LocalDate start = now.minusMonths(i).withDayOfMonth(1);
				LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

				months.add(start.format(formatter));
				privateJobs.add(jobRepository.countCustomJobsByTypeAndDate("private", start, end));
				govtJobs.add(jobRepository.countCustomJobsByTypeAndDate("govt", start, end));
				bankJobs.add(jobRepository.countCustomJobsByTypeAndDate("bank", start, end));
			}

			stats.put("months", months);
			stats.put("privateJobsData", privateJobs);
			stats.put("govtJobsData", govtJobs);
			stats.put("bankJobsData", bankJobs);

			long totalJobViews = jobViewRepository.totalJobViews();
			long todayJobViews = jobViewRepository.todayJobViews();
			long yesterdayJobViews = jobViewRepository.yesterdayJobViews();
			long weeklyJobViews = jobViewRepository.weeklyJobViews();
			long monthlyJobViews = jobViewRepository.monthlyJobViews();

			stats.put("totalJobs", totalJobs); // Total Job 
			stats.put("totalJobsViews", totalJobViews); // Total Job Views
			stats.put("todayJobsViews", todayJobViews); // Today's Job Views
			stats.put("yesterdayJobsViews", yesterdayJobViews);// Yesterday's Job Views
			stats.put("weeklyJobsViews", weeklyJobViews); // Weekly Job Views
			long totalComments = jobCommentRepository.count();
			long totalSuggestions = suggestionRepository.count();
			

			stats.put("activeUsers", activeUsers);
			stats.put("newsViews", 0);
			stats.put("newsArticles", 0);
			stats.put("jobViews", totalJobViews);
			stats.put("totalApplies", 0);
			stats.put("totalComments", totalComments);
			stats.put("totalSuggestions", totalSuggestions);
			stats.put("monthlyJobViews", monthlyJobViews);

			return ResponseEntity.ok(stats);
		} catch (Exception e) {
			logger.error("CRITICAL ERROR in getStats: ", e);
			return ResponseEntity.internalServerError().build();
		}
	}
}
