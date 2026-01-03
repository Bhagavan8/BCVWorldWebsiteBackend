package com.bcvworld.portal.repository;

import com.bcvworld.portal.model.JobComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JobCommentRepository extends JpaRepository<JobComment, Long> {
    // Fetch comments for a specific job, ordered by newest first
    List<JobComment> findByJobIdOrderByCreatedAtDesc(Long jobId);
}
