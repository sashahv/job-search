package com.olekhv.job.search.service;

import com.olekhv.job.search.auth.userCredential.UserCredential;
import com.olekhv.job.search.auth.userCredential.UserCredentialRepository;
import com.olekhv.job.search.entity.user.User;
import com.olekhv.job.search.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserCredentialRepository userCredentialRepository;

    public User editInformation(User editedUser,
                                UserCredential userCredential) {
        User authUser = userCredential.getUser();
        authUser.setFirstName(editedUser.getFirstName());
        authUser.setLastName(editedUser.getLastName());
        authUser.setPhoneNumber(editedUser.getPhoneNumber());
        authUser.setCurrentPosition(editedUser.getCurrentPosition());
        authUser.setContactEmail(editedUser.getContactEmail() != null ? editedUser.getContactEmail() : userCredential.getEmail());
        authUser.setCountry(editedUser.getCountry());
        authUser.setCity(editedUser.getCity());
        return userRepository.save(authUser);
    }
}
