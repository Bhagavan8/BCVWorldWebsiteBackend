package com.bcvworld.portal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bcvworld.portal.model.NewsArticle;
import com.bcvworld.portal.repository.NewsRepository;

@Service
public class NewsService {

    @Autowired
    private NewsRepository newsRepository;

    public NewsArticle createNews(NewsArticle newsArticle) {
        return newsRepository.save(newsArticle);
    }

    public List<NewsArticle> getAllNews() {
        return newsRepository.findAll();
    }

    public NewsArticle getNewsById(Long id) {
        return newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found"));
    }

    @Transactional
    public NewsArticle updateNews(Long id, NewsArticle updatedNews) {
        NewsArticle existing = getNewsById(id);
        existing.setTitle(updatedNews.getTitle());
        existing.setUrl(updatedNews.getUrl());
        existing.setCategory(updatedNews.getCategory());
        existing.setSection(updatedNews.getSection());
        existing.setStatus(updatedNews.getStatus());
        existing.setImageUrl(updatedNews.getImageUrl());
        
        // Update content: clear and add new to handle orphan removal correctly
        existing.getContent().clear();
        if (updatedNews.getContent() != null) {
            existing.getContent().addAll(updatedNews.getContent());
        }
        
        return newsRepository.save(existing);
    }

    public void deleteNews(Long id) {
        newsRepository.deleteById(id);
    }
}
