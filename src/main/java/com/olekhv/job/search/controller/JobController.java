package com.olekhv.job.search.controller;

import com.olekhv.job.search.auth.userCredential.UserCredential;
import com.olekhv.job.search.entity.job.Job;
import com.olekhv.job.search.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/jobs")
public class JobController {
    private final JobService jobService;

    @PostMapping("{jobId}/save")
    public ResponseEntity<List<Job>> saveJob(@PathVariable Long jobId,
                                             @AuthenticationPrincipal UserCredential userCredential){
        return ResponseEntity.ok(jobService.saveJob(jobId, userCredential));
    }

    @DeleteMapping("/saved/{jobId}/delete")
    public ResponseEntity<List<Job>> deleteSavedJob(@PathVariable Long jobId,
                                             @AuthenticationPrincipal UserCredential userCredential){
        return ResponseEntity.ok(jobService.deleteSavedJob(jobId, userCredential));
    }
}
