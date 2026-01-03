package com.bcvworld.portal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcvworld.portal.model.NewsArticlePortal;
import com.bcvworld.portal.service.NewsServicePortal;

@RestController
@RequestMapping("/api/public/news")
public class PublicNewsController {

    @Autowired
    private NewsServicePortal newsService;

    @GetMapping
    public ResponseEntity<List<NewsArticlePortal>> getAllNews() {
        return ResponseEntity.ok(newsService.getApprovedNews());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NewsArticlePortal> getNewsById(@PathVariable Long id) {
        return ResponseEntity.ok(newsService.getNewsById(id));
    }
    
    @GetMapping("/recent")
    public ResponseEntity<List<NewsArticlePortal>> getRecentNews() {
        // Implement getTop5ByStatus... in service
        return ResponseEntity.ok(newsService.getApprovedNews()); 
    }
}