package com.bcvworld.portal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bcvworld.portal.model.JobView;

public interface JobViewRepository extends JpaRepository<JobView, Long> {
    boolean existsByJobIdAndUserId(Long jobId, String userId);
}
