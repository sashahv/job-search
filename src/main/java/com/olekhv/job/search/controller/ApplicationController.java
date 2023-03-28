package com.olekhv.job.search.controller;

import com.olekhv.job.search.auth.userCredential.UserCredential;
import com.olekhv.job.search.dataobjects.EmailDO;
import com.olekhv.job.search.entity.application.Application;
import com.olekhv.job.search.entity.application.Attachment;
import com.olekhv.job.search.repository.ApplicationRepository;
import com.olekhv.job.search.service.ApplicationService;
import com.olekhv.job.search.utils.AttachmentUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<Application>> listAllApplications(@PathVariable("jobId") Long jobId,
                                                                 @AuthenticationPrincipal UserCredential userCredential){
        return ResponseEntity.ok(applicationService.listAllApplications(jobId, userCredential));
    }

    @GetMapping("/{applicationId}")
    public ResponseEntity<Application> getApplicationById(@PathVariable("applicationId") Long applicationId,
                                                            @AuthenticationPrincipal UserCredential userCredential){
        Application application = applicationService.getApplicationById(applicationId, userCredential);
        return ResponseEntity.ok(application);
    }

    @PostMapping("/job/{jobId}")
    public ResponseEntity<Application> createNewApplication(@AuthenticationPrincipal UserCredential userCredential,
                                                            @PathVariable Long jobId,
                                                            @RequestBody List<MultipartFile> multipartFile){
        return new ResponseEntity<>(applicationService.createNewApplication(userCredential, jobId, multipartFile), HttpStatus.CREATED);
    }

    @GetMapping("/attachment/{attachmentId}")
    public ResponseEntity<ByteArrayResource> watchFile(@PathVariable Long attachmentId,
                                                       @AuthenticationPrincipal UserCredential userCredential) {
        Attachment attachment = applicationService.getAttachmentById(attachmentId, userCredential);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(attachment.getFileType()))
                .body(new ByteArrayResource(AttachmentUtils.decompressFile(attachment.getData())));
    }

    @PostMapping("/{applicationId}/decline")
    public ResponseEntity<Application> declineApplication(@PathVariable Long applicationId,
                                                          @RequestBody(required = false) EmailDO emailDO,
                                                          @AuthenticationPrincipal UserCredential userCredential){
        return ResponseEntity.ok(applicationService.declineApplication(applicationId, emailDO, userCredential));
    }
}
