package com.olekhv.job.search.controller;

import com.olekhv.job.search.auth.userCredential.UserCredential;
import com.olekhv.job.search.entity.application.Application;
import com.olekhv.job.search.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/applications")
public class ApplicationController {
    private final ApplicationService applicationService;

    @GetMapping("/{applicationId}")
    public ResponseEntity<Application> fetchApplicationById(@PathVariable("applicationId") Long applicationId){
        return ResponseEntity.ok(applicationService.fetchApplicationById(applicationId));
    }

    @PostMapping
    public ResponseEntity<Application> createNewApplication(@AuthenticationPrincipal UserCredential userCredential,
                                                            @RequestParam Long jobId,
                                                            @RequestBody List<MultipartFile> multipartFile){
        return ResponseEntity.ok(applicationService.createNewApplication(userCredential, jobId, multipartFile));
    }

//    @PostMapping("/{applicationId}/file")
//    public ResponseEntity<String> attachFile(@PathVariable("applicationId") Long applicationId,
//                                             @ModelAttribute AttachmentDO attachmentDO){
//        applicationService.attachFileToApplication(applicationId, attachmentDO);
//        return ResponseEntity.ok("File successfully attached");
//    }
//
//    @PostMapping("/{jobId}/{applicationId}")
//    public ResponseEntity<Application> sendApplication(@PathVariable("jobId") Long jobId,
//                                                       @PathVariable("applicationId") Long applicationId){
//        return ResponseEntity.ok(applicationService.sendApplication(applicationId, jobId));
//    }
}
