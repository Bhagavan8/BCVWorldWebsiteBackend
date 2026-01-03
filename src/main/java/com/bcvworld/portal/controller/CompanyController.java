package com.bcvworld.portal.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.bcvworld.portal.model.Company;
import com.bcvworld.portal.model.CompanyLogo;
import com.bcvworld.portal.repository.CompanyLogoRepository;
import com.bcvworld.portal.repository.CompanyRepository;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyRepository companyRepository;
    private final CompanyLogoRepository companyLogoRepository;

    public CompanyController(CompanyRepository companyRepository, CompanyLogoRepository companyLogoRepository) {
        this.companyRepository = companyRepository;
        this.companyLogoRepository = companyLogoRepository;
    }

    @GetMapping("/search")
    public ResponseEntity<List<Company>> search(@RequestParam("q") String q) {
        return ResponseEntity.ok(companyRepository.findByNameContainingIgnoreCase(q));
    }

    @PostMapping
    public ResponseEntity<Company> create(@RequestBody Company company) {
        return ResponseEntity.ok(companyRepository.save(company));
    }

    @PostMapping("/upload-logo")
    public ResponseEntity<?> uploadLogo(@RequestParam("file") MultipartFile file) throws IOException {
        CompanyLogo logo = new CompanyLogo(file.getBytes(), file.getContentType());
        logo = companyLogoRepository.save(logo);
        
        String url = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/companies/logos/")
                .path(logo.getId().toString())
                .toUriString();
                
        return ResponseEntity.ok(java.util.Map.of("url", url));
    }
    
    @GetMapping("/logos/{id}")
    public ResponseEntity<byte[]> getLogo(@PathVariable Long id) {
        return companyLogoRepository.findById(id)
                .map(logo -> ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, logo.getContentType())
                        .body(logo.getData()))
                .orElse(ResponseEntity.notFound().build());
    }
}
