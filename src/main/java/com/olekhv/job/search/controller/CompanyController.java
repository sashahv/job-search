package com.olekhv.job.search.controller;

import com.olekhv.job.search.auth.userCredential.UserCredential;
import com.olekhv.job.search.entity.company.Company;
import com.olekhv.job.search.entity.user.User;
import com.olekhv.job.search.dataobject.CompanyDO;
import com.olekhv.job.search.service.CompanyService;
import com.olekhv.job.search.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/companies")
public class CompanyController {
    private final CompanyService companyService;
    private final JobService jobService;

    @PostMapping
    public ResponseEntity<Company> createNewCompany(@RequestBody CompanyDO companyDO,
                                                    @AuthenticationPrincipal UserCredential userCredential){
        return new ResponseEntity<>(companyService.createNewCompany(companyDO, userCredential), HttpStatus.CREATED);
    }

    @GetMapping("/{companyId}")
    public ResponseEntity<Company> fetchCompanyById(@PathVariable Long companyId){
        return ResponseEntity.ok(companyService.findCompanyById(companyId));
    }
    
    @PostMapping("/{companyId}/hiring-team")
    public ResponseEntity<List<User>> addUserToHiringTeam(@RequestParam("email") String userEmail,
                                                          @PathVariable("companyId") Long companyId,
                                                          @AuthenticationPrincipal UserCredential userCredential){
        return ResponseEntity.ok(companyService.addUserToHiringTeam(userEmail, companyId, userCredential));
    }
}
