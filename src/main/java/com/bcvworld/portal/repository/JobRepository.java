package com.bcvworld.portal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bcvworld.portal.dto.CompanyProjection;
import com.bcvworld.portal.model.Job;



@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByJobCategory(String jobCategory);
    
    @Query("SELECT j FROM Job j WHERE " +
           "(:jobCategory IS NULL OR j.jobCategory = :jobCategory) AND " +
           "(:search IS NULL OR LOWER(j.jobTitle) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(j.companyName) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<Job> searchJobs(@Param("jobCategory") String jobCategory, @Param("search") String search);

    // Admin methods
    boolean existsByReferralCode(String referralCode);
    boolean existsByJobId(String jobId);
    long countByPostedDate(java.time.LocalDate postedDate);
    long countByPostedDateBetween(java.time.LocalDate startDate, java.time.LocalDate endDate);

    @Query(value = "SELECT COUNT(*) FROM jobs WHERE job_type = :jobType AND posted_date BETWEEN :startDate AND :endDate", nativeQuery = true)
    long countCustomJobsByTypeAndDate(@Param("jobType") String jobType, @Param("startDate") java.time.LocalDate startDate, @Param("endDate") java.time.LocalDate endDate);

    @Query(value = "SELECT COUNT(*) FROM jobs WHERE job_type = :jobType", nativeQuery = true)
    long countCustomJobsByType(@Param("jobType") String jobType);
    
    @Query("""
    	    SELECT DISTINCT j
    	    FROM Job j
    	    LEFT JOIN FETCH j.locations
    	    LEFT JOIN FETCH j.educationLevels
    	    WHERE j.isActive = true
    	""")
    	List<Job> findAllWithLocationsAndEducation();
    
    @Query("""
    	    SELECT j
    	    FROM Job j
    	    LEFT JOIN FETCH j.locations
    	    LEFT JOIN FETCH j.educationLevels
    	    WHERE j.id = :id
    	""")
    	Optional<Job> findByIdWithDetails(@Param("id") Long id);
    
    @Query("""
    	    SELECT j.id
    	    FROM Job j
    	    WHERE (:search IS NULL
    	        OR LOWER(j.jobTitle) LIKE LOWER(CONCAT('%', :search, '%'))
    	        OR LOWER(j.companyName) LIKE LOWER(CONCAT('%', :search, '%')))
    	    ORDER BY j.postedDate DESC
    	""")
    	Page<Long> findJobIdsForManagement(
    	    @Param("search") String search,
    	    Pageable pageable
    	);
    @Query("""
    	    SELECT DISTINCT j
    	    FROM Job j
    	    LEFT JOIN FETCH j.locations
    	    LEFT JOIN FETCH j.educationLevels
    	    WHERE j.id IN :ids
    	""")
    	List<Job> findJobsWithDetails(@Param("ids") List<Long> ids);
    List<CompanyProjection>
    findDistinctByCompanyNameContainingIgnoreCase(String companyName);


}
