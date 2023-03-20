package com.olekhv.job.search.service;

import com.olekhv.job.search.auth.userCredential.UserCredential;
import com.olekhv.job.search.entity.education.Education;
import com.olekhv.job.search.repository.UserRepository;
import com.olekhv.job.search.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EducationService {

    private final UserRepository userRepository;

    public List<Education> addNewEducation(Education education,
                                           UserCredential userCredential){
        User authUser = userCredential.getUser();
        List<Education> userEducations = authUser.getEducations();
        userEducations.add(education);
        userRepository.save(authUser);
        return userEducations;
    }
}
