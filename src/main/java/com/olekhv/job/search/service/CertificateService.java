package com.olekhv.job.search.service;

import com.olekhv.job.search.auth.userCredential.UserCredential;
import com.olekhv.job.search.auth.userCredential.UserCredentialRepository;
import com.olekhv.job.search.entity.certificate.Certificate;
import com.olekhv.job.search.entity.education.Education;
import com.olekhv.job.search.entity.user.User;
import com.olekhv.job.search.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CertificateService {

    private final UserRepository userRepository;
    private final UserCredentialRepository userCredentialRepository;

    public List<Certificate> listAllCertificatesOfUser(String userEmail){
        UserCredential userCredential = userCredentialRepository.findByEmail(userEmail).orElseThrow(
                () -> new UsernameNotFoundException("User with email " + userEmail + " does not exist")
        );
        User user = userCredential.getUser();

        return user.getCertificates();
    }

    public List<Certificate> addNewCertificate(Certificate certificate,
                                               UserCredential userCredential){
        User authUser = userCredential.getUser();
        List<Certificate> userCertificates = authUser.getCertificates();
        userCertificates.add(certificate);
        userRepository.save(authUser);
        return userCertificates;
    }
}
