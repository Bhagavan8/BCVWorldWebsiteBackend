package com.bcvworld.portal.repository;

import com.bcvworld.portal.model.WebsiteTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WebsiteTransactionRepository extends JpaRepository<WebsiteTransaction, Long> {
}

