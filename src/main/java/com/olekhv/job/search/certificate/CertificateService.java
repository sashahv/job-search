package com.olekhv.job.search.certificate;

import com.olekhv.job.search.auth.userCredential.UserCredential;
import com.olekhv.job.search.user.User;
import com.olekhv.job.search.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CertificateService {

    private final UserRepository userRepository;

    public List<Certificate> addNewCertificate(Certificate certificate,
                                               UserCredential userCredential){
        User authUser = userCredential.getUser();
        List<Certificate> userCertificates = authUser.getCertificates();
        userCertificates.add(certificate);
        userRepository.save(authUser);
        return userCertificates;
    }
}
