package com.bcvworld.portal.repository;

import com.bcvworld.portal.model.Subscription;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
	   
}

