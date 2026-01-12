package com.bcvworld.portal.model;

import jakarta.persistence.*;

@Entity
@Table(name = "company_logo")
public class CompanyLogo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "data", columnDefinition = "LONGBLOB")
    private byte[] data;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    // ✅ REQUIRED BY HIBERNATE
    protected CompanyLogo() {
    }

    // ✅ USED BY CONTROLLER
    public CompanyLogo(byte[] data, String contentType) {
        this.data = data;
        this.contentType = contentType;
    }

    public Long getId() {
        return id;
    }

    public byte[] getData() {
        return data;
    }

    public String getContentType() {
        return contentType;
    }
}
