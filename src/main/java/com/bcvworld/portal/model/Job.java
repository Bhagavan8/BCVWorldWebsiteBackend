package com.bcvworld.portal.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "jobs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "job_id", unique = true, length = 100)
    private String jobId;

    @Column(name = "job_title", nullable = false)
    private String jobTitle;

    @Column(name = "job_category", nullable = false)
    private String jobCategory;

    @Column(name = "job_type", nullable = false)
    private String jobType;

    private String employmentType;
    private String companyName;
    private String companyWebsite;

    @Column(name = "company_logo_url")
    private String logoUrl;
    
    @Column(name = "company_logo_id")
    private Long companyLogoId; 

    @Lob
    private String aboutCompany;

    @Lob
    private String description;

    @Lob
    private String details;

    @Lob
    private String skills;

    @Lob
    private String qualifications;

    @Lob
    private String walkinDetails;

    private boolean useExistingCompany;
    private Long companyId;
    
    @Lob
    @Column(name = "company_logo", columnDefinition = "LONGBLOB") // For MySQL
    private byte[] companyLogo;

    // ✅ FETCHED BY JOIN
    @ElementCollection
    @CollectionTable(name = "job_locations", joinColumns = @JoinColumn(name = "job_id"))
    @Column(name = "location")
    private Set<String> locations = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "job_education_levels", joinColumns = @JoinColumn(name = "job_id"))
    @Column(name = "education_level")
    private Set<String> educationLevels = new HashSet<>();


    @Column(name = "experience_required", nullable = false, length = 100)
    private String experienceRequired;

    @Column(name = "salary_range")
    private String salary;

    private String noticePeriod;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate postedDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate lastDateToApply;

    private String applicationLink;
    private String applicationEmail;
    private String applicationMethod;
    private String listingStatus;

    private boolean isActive = true;
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

	


	public Set<String> getLocations() {
		return locations;
	}

	public void setLocations(Set<String> locations) {
		this.locations = locations;
	}

	public Set<String> getEducationLevels() {
		return educationLevels;
	}

	public void setEducationLevels(Set<String> educationLevels) {
		this.educationLevels = educationLevels;
	}

	public String getExperienceRequired() {
	    return experienceRequired;
	}

	public void setExperienceRequired(String experienceRequired) {
	    this.experienceRequired = experienceRequired;
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

	public byte[] getCompanyLogo() {
		return companyLogo;
	}

	public void setCompanyLogo(byte[] companyLogo) {
		this.companyLogo = companyLogo;
	}

	public Long getCompanyLogoId() {
		return companyLogoId;
	}

	public void setCompanyLogoId(Long companyLogoId) {
		this.companyLogoId = companyLogoId;
	}
    
}
