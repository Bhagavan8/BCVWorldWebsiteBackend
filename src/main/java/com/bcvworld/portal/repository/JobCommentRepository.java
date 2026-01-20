package com.bcvworld.portal.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bcvworld.portal.model.JobComment;

@Repository
public interface JobCommentRepository extends JpaRepository<JobComment, Long> {
    // Fetch comments for a specific job, ordered by newest first
    List<JobComment> findByJobIdOrderByCreatedAtDesc(Long jobId);
    @Query("""
    		   SELECT c
    		   FROM JobComment c
    		   WHERE LOWER(c.content) LIKE LOWER(CONCAT('%', :search, '%'))
    		      OR LOWER(c.userName) LIKE LOWER(CONCAT('%', :search, '%'))
    		      OR LOWER(c.userEmail) LIKE LOWER(CONCAT('%', :search, '%'))
    		""")
    		Page<JobComment> searchComments(
    		        @Param("search") String search,
    		        Pageable pageable
    		);

}
