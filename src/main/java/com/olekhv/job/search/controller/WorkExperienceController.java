package com.olekhv.job.search.controller;

import com.olekhv.job.search.auth.userCredential.UserCredential;
import com.olekhv.job.search.entity.workExperience.WorkExperience;
import com.olekhv.job.search.service.WorkExperienceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/{userEmail}/work-experience")
public class WorkExperienceController {
    private final WorkExperienceService workExperienceService;

    @PostMapping
    public ResponseEntity<List<WorkExperience>> addNewWorkExperience(@RequestBody WorkExperience workExperience,
                                                                     @PathVariable String userEmail,
                                                                     @AuthenticationPrincipal UserCredential userCredential) {
        userEmail = userCredential.getEmail();
        return ResponseEntity.ok(workExperienceService.addNewWorkExperience(workExperience, userCredential));
    }

    @GetMapping
    public ResponseEntity<List<WorkExperience>> listAllLanguagesOfUser(@PathVariable String userEmail){
        return ResponseEntity.ok(workExperienceService.listAllWorkExperiencesOfUser(userEmail));
    }
}
