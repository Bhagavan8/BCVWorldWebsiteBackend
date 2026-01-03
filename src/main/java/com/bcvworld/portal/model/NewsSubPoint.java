package com.bcvworld.portal.model;



import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class NewsSubPoint {
    private String text;
    private boolean isBold;
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
    
}