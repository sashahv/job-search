package com.olekhv.job.search.controller;

import com.olekhv.job.search.auth.userCredential.UserCredential;
import com.olekhv.job.search.dataobjects.JobDO;
import com.olekhv.job.search.entity.company.Company;
import com.olekhv.job.search.entity.job.Job;
import com.olekhv.job.search.entity.user.User;
import com.olekhv.job.search.dataobjects.CompanyDO;
import com.olekhv.job.search.service.CompanyService;
import com.olekhv.job.search.service.JobService;
import lombok.RequiredArgsConstructor;
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
        return ResponseEntity.ok(companyService.createNewCompany(companyDO, userCredential));
    }

    @GetMapping("/{companyId}")
    public ResponseEntity<Company> fetchCompanyById(@PathVariable Long companyId){
        return ResponseEntity.ok(companyService.fetchCompanyById(companyId));
    }
    
    @PostMapping("/{companyId}/hiring-team")
    public ResponseEntity<List<User>> addUserToHiringTeam(@RequestParam("email") String userEmail,
                                                          @PathVariable("companyId") Long companyId,
                                                          @AuthenticationPrincipal UserCredential userCredential){
        return ResponseEntity.ok(companyService.addUserToHiringTeam(userEmail, companyId, userCredential));
    }

    @PostMapping("/{companyId}/jobs")
    public ResponseEntity<Job> createJob(@RequestBody JobDO jobDO,
                                         @PathVariable Long companyId,
                                         @AuthenticationPrincipal UserCredential userCredential){
        return ResponseEntity.ok(jobService.createNewJob(jobDO, companyId, userCredential));
    }
}
