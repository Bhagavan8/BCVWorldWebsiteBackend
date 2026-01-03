package com.bcvworld.portal.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "news_paragraphs")
public class NewsParagraphPortal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_article_id")
    @JsonIgnore
    private NewsArticlePortal newsArticle;

    @OneToMany(mappedBy = "newsParagraph", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NewsSubPointPortal> subPoints = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public NewsArticlePortal getNewsArticle() {
		return newsArticle;
	}

	public void setNewsArticle(NewsArticlePortal newsArticle) {
		this.newsArticle = newsArticle;
	}

	public List<NewsSubPointPortal> getSubPoints() {
		return subPoints;
	}

	public void setSubPoints(List<NewsSubPointPortal> subPoints) {
		this.subPoints = subPoints;
	}
    
}