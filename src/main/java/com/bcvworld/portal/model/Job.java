package com.bcvworld.portal.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "jobs")
public class Job {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true)
	private String jobId;
	@Column(name = "job_title", nullable = false)
	private String jobTitle;
	@Column(name = "job_category", nullable = false)
	private String jobCategory;
	@Column(name = "job_type", nullable = false)
	private String jobType;
	private String employmentType;
	@Column(name = "company_name")
	private String companyName;
	@Column(name = "company_website")
	private String companyWebsite;
	@Column(name = "company_logo_url")
	private String logoUrl;
	@Column(length = 5000, name = "about_company")
	private String aboutCompany;
	private boolean useExistingCompany;
	private Long companyId;
	@ElementCollection
	@CollectionTable(name = "job_locations", joinColumns = @JoinColumn(name = "job_id"))
	@Column(name = "location")
	private List<String> locations;
	@ElementCollection
	@CollectionTable(name = "job_education_levels", joinColumns = @JoinColumn(name = "job_id"))
	@Column(name = "education_level")
	private List<String> educationLevels;
	@Column(length = 5000, nullable = false)
	private String description;
	@Column(length = 5000)
	private String details;
	@Column(length = 5000, nullable = false)
	private String skills;
	@Column(length = 5000, nullable = false)
	private String qualifications;
	@Column(name = "experience_required", nullable = false)
	@JsonProperty("experienceRequired")
	private String experience;
	@Column(name = "salary_range")
	private String salary;
	private String noticePeriod;
	@Column(columnDefinition = "TEXT")
	private String walkinDetails;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate postedDate;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate lastDateToApply;
	@Column(name = "application_link")
	private String applicationLink;
	@Column(name = "application_email")
	private String applicationEmail;
	private String applicationMethod;
	private String listingStatus;
	private boolean isActive = true;
	@Column(name = "referral_code")
	private String referralCode;
	private Integer viewCount = 0;
	private Integer likeCount = 0;
	@Transient
	private boolean isLiked = false;

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

	public String getEmploymentType() {
		return employmentType;
	}

	public void setEmploymentType(String employmentType) {
		this.employmentType = employmentType;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyWebsite() {
		return companyWebsite;
	}

	public void setCompanyWebsite(String companyWebsite) {
		this.companyWebsite = companyWebsite;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getAboutCompany() {
		return aboutCompany;
	}

	public void setAboutCompany(String aboutCompany) {
		this.aboutCompany = aboutCompany;
	}

	public boolean isUseExistingCompany() {
		return useExistingCompany;
	}

	public void setUseExistingCompany(boolean useExistingCompany) {
		this.useExistingCompany = useExistingCompany;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getSkills() {
		return skills;
	}

	public void setSkills(String skills) {
		this.skills = skills;
	}

	public String getQualifications() {
		return qualifications;
	}

	public void setQualifications(String qualifications) {
		this.qualifications = qualifications;
	}

	public String getExperience() {
		return experience;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}

	public String getSalary() {
		return salary;
	}

	public void setSalary(String salary) {
		this.salary = salary;
	}

	public String getNoticePeriod() {
		return noticePeriod;
	}

	public void setNoticePeriod(String noticePeriod) {
		this.noticePeriod = noticePeriod;
	}

	public String getWalkinDetails() {
		return walkinDetails;
	}

	public void setWalkinDetails(String walkinDetails) {
		this.walkinDetails = walkinDetails;
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

	public String getApplicationLink() {
		return applicationLink;
	}

	public void setApplicationLink(String applicationLink) {
		this.applicationLink = applicationLink;
	}

	public String getApplicationEmail() {
		return applicationEmail;
	}

	public void setApplicationEmail(String applicationEmail) {
		this.applicationEmail = applicationEmail;
	}

	public String getApplicationMethod() {
		return applicationMethod;
	}

	public void setApplicationMethod(String applicationMethod) {
		this.applicationMethod = applicationMethod;
	}

	public String getListingStatus() {
		return listingStatus;
	}

	public void setListingStatus(String listingStatus) {
		this.listingStatus = listingStatus;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getReferralCode() {
		return referralCode;
	}

	public void setReferralCode(String referralCode) {
		this.referralCode = referralCode;
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

	public boolean isLiked() {
		return isLiked;
	}

	public void setLiked(boolean isLiked) {
		this.isLiked = isLiked;
	}
}