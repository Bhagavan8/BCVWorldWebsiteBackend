package com.bcvworld.portal.service;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bcvworld.portal.model.NewsArticlePortal;
import com.bcvworld.portal.model.NewsParagraphPortal;
import com.bcvworld.portal.model.NewsSubPointPortal;
import com.bcvworld.portal.repository.NewsRepositoryPortal;

@Service
public class NewsServicePortal {

    @Autowired
    private NewsRepositoryPortal newsRepository;

    @Transactional
    public NewsArticlePortal createNews(NewsArticlePortal newsArticle) {
        // Set relationships manually to ensure persistence works correctly
        if (newsArticle.getContent() != null) {
            for (NewsParagraphPortal paragraph : newsArticle.getContent()) {
                paragraph.setNewsArticle(newsArticle);
                if (paragraph.getSubPoints() != null) {
                    for (NewsSubPointPortal subPoint : paragraph.getSubPoints()) {
                        subPoint.setNewsParagraph(paragraph);
                    }
                }
            }
        }
        return newsRepository.save(newsArticle);
    }

    public List<NewsArticlePortal> getAllNews() {
        return newsRepository.findAll();
    }

    public List<NewsArticlePortal> getApprovedNews() {
        return newsRepository.findByStatusOrderByCreatedAtDesc("approved");
    }

    public NewsArticlePortal getNewsById(Long id) {
        return newsRepository.findById(id).orElseThrow(() -> new RuntimeException("News not found"));
    }

    @Transactional
    public void deleteNews(Long id) {
        newsRepository.deleteById(id);
    }
}