package com.bcvworld.portal.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "jobs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // External / display job id
    @Column(name = "job_id", unique = true, length = 100)
    private String jobId;

    @Column(name = "job_title", nullable = false, length = 255)
    private String jobTitle;

    @Column(name = "job_category", nullable = false, length = 100)
    private String jobCategory;

    @Column(name = "job_type", nullable = false, length = 100)
    private String jobType;

    @Column(name = "employment_type", length = 100)
    private String employmentType;

    @Column(name = "company_name", length = 255)
    private String companyName;

    @Column(name = "company_website", length = 255)
    private String companyWebsite;

    @Column(name = "company_logo_url", length = 255)
    private String logoUrl;

    // ---------- LONG TEXT FIELDS (TEXT / LOB) ----------
    @Lob
    @Column(name = "about_company", columnDefinition = "TEXT")
    private String aboutCompany;

    @Lob
    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;

    @Lob
    @Column(name = "details", columnDefinition = "TEXT")
    private String details;

    @Lob
    @Column(name = "skills", columnDefinition = "TEXT", nullable = false)
    private String skills;

    @Lob
    @Column(name = "qualifications", columnDefinition = "TEXT", nullable = false)
    private String qualifications;

    @Lob
    @Column(name = "walkin_details", columnDefinition = "TEXT")
    private String walkinDetails;
    // --------------------------------------------------

    private boolean useExistingCompany;

    private Long companyId;

    // ---------- ELEMENT COLLECTIONS ----------
    @ElementCollection
    @CollectionTable(
            name = "job_locations",
            joinColumns = @JoinColumn(name = "job_id")
    )
    @Column(name = "location", length = 255)
    private List<String> locations;

    @ElementCollection
    @CollectionTable(
            name = "job_education_levels",
            joinColumns = @JoinColumn(name = "job_id")
    )
    @Column(name = "education_level", length = 255)
    private List<String> educationLevels;
    // ----------------------------------------

    @Column(name = "experience_required", nullable = false, length = 100)
    @JsonProperty("experienceRequired")
    private String experience;

    @Column(name = "salary_range", length = 100)
    private String salary;

    @Column(name = "notice_period", length = 100)
    private String noticePeriod;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate postedDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate lastDateToApply;

    @Column(name = "application_link", length = 255)
    private String applicationLink;

    @Column(name = "application_email", length = 255)
    private String applicationEmail;

    @Column(name = "application_method", length = 100)
    private String applicationMethod;

    @Column(name = "listing_status", length = 100)
    private String listingStatus;

    private boolean isActive = true;

    @Column(name = "referral_code", length = 100)
    private String referralCode;

    private Integer viewCount = 0;
    private Integer likeCount = 0;

    // UI-only field (not persisted)
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

	public String getWalkinDetails() {
		return walkinDetails;
	}

	public void setWalkinDetails(String walkinDetails) {
		this.walkinDetails = walkinDetails;
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
