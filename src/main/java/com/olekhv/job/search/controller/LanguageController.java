package com.olekhv.job.search.controller;

import com.olekhv.job.search.auth.userCredential.UserCredential;
import com.olekhv.job.search.service.LanguageService;
import com.olekhv.job.search.entity.language.UserLanguage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/languages")
public class LanguageController {
    private final LanguageService languageService;

    @PostMapping("/add")
    public ResponseEntity<List<UserLanguage>> addLanguageToUser(@RequestBody UserLanguage language,
                                                                @AuthenticationPrincipal UserCredential userCredential){
        return ResponseEntity.ok(languageService.addLanguageToUser(language, userCredential));
    }
}
