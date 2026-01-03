package com.bcvworld.portal.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bcvworld.portal.model.NewsArticlePortal;

@Repository
public interface NewsRepositoryPortal extends JpaRepository<NewsArticlePortal, Long> {
    List<NewsArticlePortal> findByStatusOrderByCreatedAtDesc(String status);
    List<NewsArticlePortal> findTop5ByStatusOrderByCreatedAtDesc(String status);
}
