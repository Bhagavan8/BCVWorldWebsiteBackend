package com.bcvworld.portal.model;



import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class NewsParagraph {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String text;

    @ElementCollection
    @CollectionTable(name = "news_paragraph_subpoints", joinColumns = @JoinColumn(name = "paragraph_id"))
    private List<NewsSubPoint> subPoints = new ArrayList<>();

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

	public List<NewsSubPoint> getSubPoints() {
		return subPoints;
	}

	public void setSubPoints(List<NewsSubPoint> subPoints) {
		this.subPoints = subPoints;
	}
    
}