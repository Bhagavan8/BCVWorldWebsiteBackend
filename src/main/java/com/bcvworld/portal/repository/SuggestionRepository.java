package com.bcvworld.portal.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bcvworld.portal.model.ContactMessage;
import com.bcvworld.portal.model.Suggestion;

@Repository
public interface SuggestionRepository extends JpaRepository<Suggestion, Long> {
	Page<Suggestion> findAllByOrderByDateDesc(Pageable pageable);
	long countByDateAfter(LocalDateTime date);

}
