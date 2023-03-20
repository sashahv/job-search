package com.olekhv.job.search.service;

import com.olekhv.job.search.auth.userCredential.UserCredential;
import com.olekhv.job.search.repository.LanguageRepository;
import com.olekhv.job.search.entity.language.UserLanguage;
import com.olekhv.job.search.entity.user.User;
import com.olekhv.job.search.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class LanguageService {
    private final UserRepository userRepository;
    private final LanguageRepository languageRepository;

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
}
