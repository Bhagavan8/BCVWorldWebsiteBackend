package com.bcvworld.portal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bcvworld.portal.model.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    List<Company> findByNameContainingIgnoreCase(String q);
}
