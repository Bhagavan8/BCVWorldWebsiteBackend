package com.bcvworld.portal.dto;

import java.util.List;

public class JobDTO {
    private Long id;
    private String jobTitle;
    private String companyName;
    private List<JobLocationDTO> locations;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getJobTitle() {
		return jobTitle;
	}
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public List<JobLocationDTO> getLocations() {
		return locations;
	}
	public void setLocations(List<JobLocationDTO> locations) {
		this.locations = locations;
	}
    
    
}

