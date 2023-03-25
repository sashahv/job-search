package com.olekhv.job.search.service;

import com.olekhv.job.search.auth.userCredential.UserCredential;
import com.olekhv.job.search.auth.userCredential.UserCredentialRepository;
import com.olekhv.job.search.entity.language.Language;
import com.olekhv.job.search.entity.skill.Skill;
import com.olekhv.job.search.repository.LanguageRepository;
import com.olekhv.job.search.entity.language.UserLanguage;
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
public class LanguageService {
    private final UserRepository userRepository;
    private final UserCredentialRepository userCredentialRepository;

    public List<UserLanguage> listAllLanguagesOfUser(String userEmail){
        UserCredential userCredential = findUserCredentialByEmail(userEmail);
        User user = userCredential.getUser();
        return user.getLanguages();
    }

    public List<UserLanguage> addLanguageToUser(UserLanguage userLanguage,
                                                UserCredential userCredential){
        User authUser = userCredential.getUser();
        List<UserLanguage> userLanguages = authUser.getLanguages();
        if(!userLanguages.contains(userLanguage)){
            userLanguages.add(userLanguage);
        }
        userRepository.save(authUser);
        return userLanguages;
    }

    private UserCredential findUserCredentialByEmail(String userEmail) {
        return userCredentialRepository.findByEmail(userEmail).orElseThrow(
                () -> new UsernameNotFoundException("User with email " + userEmail + " does not exist")
        );
    }
}
