package com.olekhv.job.search.service;

import com.olekhv.job.search.auth.userCredential.UserCredential;
import com.olekhv.job.search.auth.userCredential.UserCredentialRepository;
import com.olekhv.job.search.entity.education.Education;
import com.olekhv.job.search.entity.language.UserLanguage;
import com.olekhv.job.search.repository.UserRepository;
import com.olekhv.job.search.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EducationService {

    private final UserRepository userRepository;
    private final UserCredentialRepository userCredentialRepository;

    public List<Education> listAllEducationsOfUser(String userEmail){
        UserCredential userCredential = findUserCredentialByEmail(userEmail);
        User user = userCredential.getUser();
        return user.getEducations();
    }

    public List<Education> addNewEducation(Education education,
                                           UserCredential userCredential){
        User authUser = userCredential.getUser();
        List<Education> userEducations = authUser.getEducations();
        userEducations.add(education);
        userRepository.save(authUser);
        return userEducations;
    }

    private UserCredential findUserCredentialByEmail(String userEmail) {
        return userCredentialRepository.findByEmail(userEmail).orElseThrow(
                () -> new UsernameNotFoundException("User with email " + userEmail + " does not exist")
        );
    }
}
