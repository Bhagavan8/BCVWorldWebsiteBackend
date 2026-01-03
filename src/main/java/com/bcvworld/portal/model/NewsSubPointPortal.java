package com.bcvworld.portal.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "news_sub_points")
public class NewsSubPointPortal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String text;

    private boolean isBold;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_paragraph_id")
    @JsonIgnore
    private NewsParagraphPortal newsParagraph;

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

	public boolean isBold() {
		return isBold;
	}

	public void setBold(boolean isBold) {
		this.isBold = isBold;
	}

	public NewsParagraphPortal getNewsParagraph() {
		return newsParagraph;
	}

	public void setNewsParagraph(NewsParagraphPortal newsParagraph) {
		this.newsParagraph = newsParagraph;
	}
    
}
