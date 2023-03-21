package com.olekhv.job.search.controller;

import com.olekhv.job.search.auth.userCredential.UserCredential;
import com.olekhv.job.search.entity.company.Company;
import com.olekhv.job.search.model.CompanyModel;
import com.olekhv.job.search.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/companies")
public class CompanyController {
    private final CompanyService companyService;

    @PostMapping("/create")
    public ResponseEntity<Company> createNewCompany(@RequestBody CompanyModel companyModel,
                                                    @AuthenticationPrincipal UserCredential userCredential){
        return ResponseEntity.ok(companyService.createNewCompany(companyModel, userCredential));
    }
}
