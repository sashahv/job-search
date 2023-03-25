package com.olekhv.job.search.controller;

import com.olekhv.job.search.auth.userCredential.UserCredential;
import com.olekhv.job.search.entity.education.Education;
import com.olekhv.job.search.entity.language.UserLanguage;
import com.olekhv.job.search.service.EducationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class EducationController {

    private final EducationService educationService;

    @PostMapping("/educations")
    public ResponseEntity<List<Education>> addNewEducation(@RequestBody Education education,
                                                           @AuthenticationPrincipal UserCredential userCredential) {
        return ResponseEntity.ok(educationService.addNewEducation(education, userCredential));
    }

    @GetMapping("/{userEmail}/educations")
    public ResponseEntity<List<Education>> listAllLanguagesOfUser(@PathVariable String userEmail){
        return ResponseEntity.ok(educationService.listAllEducationsOfUser(userEmail));
    }
}
