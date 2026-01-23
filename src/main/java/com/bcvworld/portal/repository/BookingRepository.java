package com.bcvworld.portal.repository;




import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bcvworld.portal.model.MentorshipBooking;

@Repository
public interface BookingRepository extends JpaRepository<MentorshipBooking, Long> {
    // To check if a transaction ID is already used
    boolean existsByTransactionId(String transactionId);
    
    Optional<MentorshipBooking> findByTransactionId(String transactionId);
 
    List<MentorshipBooking> findAllByOrderByCreatedAtDesc();
}