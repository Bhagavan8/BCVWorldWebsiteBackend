package com.bcvworld.portal.dto;

import java.time.LocalDate;
import java.util.List;

public class JobResponse {

    private Long id;
    private String jobId;
    private String jobTitle;
    private String jobCategory;
    private String jobType;
    private String companyName;
    private String salary;
    private Integer viewCount;
    private Integer likeCount;
    private List<String> locations;
    private List<String> educationLevels;
    private LocalDate postedDate;
    private LocalDate lastDateToApply;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getJobId() {
		return jobId;
	}
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	public String getJobTitle() {
		return jobTitle;
	}
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	public String getJobCategory() {
		return jobCategory;
	}
	public void setJobCategory(String jobCategory) {
		this.jobCategory = jobCategory;
	}
	public String getJobType() {
		return jobType;
	}
	public void setJobType(String jobType) {
		this.jobType = jobType;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getSalary() {
		return salary;
	}
	public void setSalary(String salary) {
		this.salary = salary;
	}
	public Integer getViewCount() {
		return viewCount;
	}
	public void setViewCount(Integer viewCount) {
		this.viewCount = viewCount;
	}
	public Integer getLikeCount() {
		return likeCount;
	}
	public void setLikeCount(Integer likeCount) {
		this.likeCount = likeCount;
	}
	public List<String> getLocations() {
		return locations;
	}
	public void setLocations(List<String> locations) {
		this.locations = locations;
	}
	public List<String> getEducationLevels() {
		return educationLevels;
	}
	public void setEducationLevels(List<String> educationLevels) {
		this.educationLevels = educationLevels;
	}
	public LocalDate getPostedDate() {
		return postedDate;
	}
	public void setPostedDate(LocalDate postedDate) {
		this.postedDate = postedDate;
	}
	public LocalDate getLastDateToApply() {
		return lastDateToApply;
	}
	public void setLastDateToApply(LocalDate lastDateToApply) {
		this.lastDateToApply = lastDateToApply;
	}

    
}
