package com.olekhv.job.search.controller;

import com.olekhv.job.search.auth.userCredential.UserCredential;
import com.olekhv.job.search.dataobject.JobDO;
import com.olekhv.job.search.datatransferobject.JobResponse;
import com.olekhv.job.search.entity.job.Job;
import com.olekhv.job.search.entity.job.JobFilterFields;
import com.olekhv.job.search.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/jobs")
public class JobController {
    private final JobService jobService;

    @PostMapping
    public ResponseEntity<Job> createJob(@RequestBody JobDO jobDO,
                                         @RequestParam Long companyId,
                                         @AuthenticationPrincipal UserCredential userCredential){
        return new ResponseEntity<>(jobService.createNewJob(jobDO, companyId, userCredential), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<JobResponse>> listAllByPage(@RequestParam(value = "page", required = false, defaultValue = "1") Integer pageNumber,
                                                           @RequestParam(required = false, defaultValue = "createdAt") String sortField,
                                                           @RequestParam(required = false, defaultValue = "desc") String sortDir,
                                                           @RequestParam(required = false) String keyword,
                                                           JobFilterFields jobFilterFields){
        return ResponseEntity.ok(jobService.listAllJobs(pageNumber, sortField, sortDir, keyword, jobFilterFields).getContent());
    }

    @PutMapping("/job/{jobId}/extend")
    public ResponseEntity<Job> extendJobRecruitmentTerm(@PathVariable Long jobId,
                                                        @AuthenticationPrincipal UserCredential userCredential){
        return ResponseEntity.ok(jobService.extendJobRecruitmentTerm(jobId, userCredential));
    }

    @PostMapping("{jobId}/save")
    public ResponseEntity<List<Job>> saveJob(@PathVariable Long jobId,
                                             @AuthenticationPrincipal UserCredential userCredential){
        return ResponseEntity.ok(jobService.saveJob(jobId, userCredential));
    }

    @DeleteMapping("/saved/{jobId}/delete")
    public ResponseEntity<String> deleteSavedJob(@PathVariable Long jobId,
                                             @AuthenticationPrincipal UserCredential userCredential){
        jobService.deleteSavedJob(jobId, userCredential);
        return ResponseEntity.ok("Job with id + " + jobId + " successfully deleted");
    }
}
