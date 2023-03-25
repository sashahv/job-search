package com.olekhv.job.search.controller;

import com.olekhv.job.search.auth.userCredential.UserCredential;
import com.olekhv.job.search.service.LanguageService;
import com.olekhv.job.search.entity.language.UserLanguage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class LanguageController {
    private final LanguageService languageService;

    @PostMapping("/languages")
    public ResponseEntity<List<UserLanguage>> addLanguageToUser(@RequestBody UserLanguage language,
                                                                @AuthenticationPrincipal UserCredential userCredential) {
        return ResponseEntity.ok(languageService.addLanguageToUser(language, userCredential));
    }

    @GetMapping("/{userEmail}/languages")
    public ResponseEntity<List<UserLanguage>> listAllLanguagesOfUser(@PathVariable String userEmail){
        return ResponseEntity.ok(languageService.listAllLanguagesOfUser(userEmail));
    }
}
