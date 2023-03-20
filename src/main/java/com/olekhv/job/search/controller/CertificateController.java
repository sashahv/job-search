package com.olekhv.job.search.controller;

import com.olekhv.job.search.auth.userCredential.UserCredential;
import com.olekhv.job.search.entity.certificate.Certificate;
import com.olekhv.job.search.service.CertificateService;
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
@RequestMapping("/api/v1/user/certificates")
public class CertificateController {
    private final CertificateService certificateService;

    @PostMapping("/add")
    public ResponseEntity<List<Certificate>> addNewCertificate(@RequestBody Certificate certificate,
                                                               @AuthenticationPrincipal UserCredential userCredential){
        return ResponseEntity.ok(certificateService.addNewCertificate(certificate, userCredential));
    }
}
