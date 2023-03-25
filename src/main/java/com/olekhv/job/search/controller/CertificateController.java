package com.olekhv.job.search.controller;

import com.olekhv.job.search.auth.userCredential.UserCredential;
import com.olekhv.job.search.entity.certificate.Certificate;
import com.olekhv.job.search.entity.language.UserLanguage;
import com.olekhv.job.search.service.CertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class CertificateController {
    private final CertificateService certificateService;

    @PostMapping("/certificates")
    public ResponseEntity<List<Certificate>> addNewCertificate(@RequestBody Certificate certificate,
                                                               @AuthenticationPrincipal UserCredential userCredential) {
        return new ResponseEntity<>(certificateService.addNewCertificate(certificate, userCredential), HttpStatus.CREATED);
    }

    @GetMapping("/{userEmail}/certificates")
    public ResponseEntity<List<Certificate>> listAllLanguagesOfUser(@PathVariable String userEmail){
        return ResponseEntity.ok(certificateService.listAllCertificatesOfUser(userEmail));
    }
}
