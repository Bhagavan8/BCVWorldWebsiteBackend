package com.bcvworld.portal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bcvworld.portal.model.JobView;

public interface JobViewRepository extends JpaRepository<JobView, Long> {
    boolean existsByJobIdAndUserId(Long jobId, String userId);
    @Query(value = "SELECT COUNT(*) FROM job_views", nativeQuery = true)
    long totalJobViews();

    @Query(value = """
        SELECT COUNT(*) 
        FROM job_views 
        WHERE DATE(viewed_at) = CURDATE()
        """, nativeQuery = true)
    long todayJobViews();

    @Query(value = """
        SELECT COUNT(*) 
        FROM job_views 
        WHERE DATE(viewed_at) = CURDATE() - INTERVAL 1 DAY
        """, nativeQuery = true)
    long yesterdayJobViews();

    @Query(value = """
        SELECT COUNT(*) 
        FROM job_views 
        WHERE viewed_at >= CURDATE() - INTERVAL 7 DAY
        """, nativeQuery = true)
    long weeklyJobViews();
    
    @Query(value = """
            SELECT COUNT(*)
            FROM job_views
            WHERE MONTH(viewed_at) = MONTH(CURDATE())
              AND YEAR(viewed_at) = YEAR(CURDATE())
            """, nativeQuery = true)
        long monthlyJobViews();
}
