package com.olekhv.job.search.service;

import com.olekhv.job.search.auth.userCredential.UserCredential;
import com.olekhv.job.search.auth.userCredential.UserCredentialRepository;
import com.olekhv.job.search.entity.education.Education;
import com.olekhv.job.search.entity.user.User;
import com.olekhv.job.search.entity.user.WorkExperience;
import com.olekhv.job.search.repository.UserRepository;
import com.olekhv.job.search.repository.WorkExperienceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class WorkExperienceService {
    private final UserRepository userRepository;
    private final UserCredentialRepository userCredentialRepository;

    public List<WorkExperience> listAllWorkExperiencesOfUser(String userEmail){
        UserCredential userCredential = findUserCredentialByEmail(userEmail);
        User user = userCredential.getUser();
        return user.getWorkExperiences();
    }

    public List<WorkExperience> addNewWorkExperience(WorkExperience workExperience,
                                                     UserCredential userCredential){
        User authUser = userCredential.getUser();
        List<WorkExperience> userEducations = authUser.getWorkExperiences();
        userEducations.add(workExperience);
        userRepository.save(authUser);
        return userEducations;
    }

    private UserCredential findUserCredentialByEmail(String userEmail) {
        return userCredentialRepository.findByEmail(userEmail).orElseThrow(
                () -> new UsernameNotFoundException("User with email " + userEmail + " does not exist")
        );
    }
}
