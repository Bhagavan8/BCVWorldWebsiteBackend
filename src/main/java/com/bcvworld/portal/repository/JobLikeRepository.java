package com.bcvworld.portal.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.bcvworld.portal.model.JobLike;

public interface JobLikeRepository extends JpaRepository<JobLike, Long> {
    Optional<JobLike> findByJobIdAndUserId(Long jobId, String userId);
    boolean existsByJobIdAndUserId(Long jobId, String userId);
}
