package com.bcvworld.portal.repository;

import com.bcvworld.portal.model.FinanceTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinanceTransactionRepository extends JpaRepository<FinanceTransaction, Long> {
}

