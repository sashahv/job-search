package com.olekhv.job.search.user;

import com.olekhv.job.search.auth.userCredential.UserCredential;
import com.olekhv.job.search.certificate.Certificate;
import com.olekhv.job.search.skill.Skill;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    // TODO: controller
    public void editInformation(User editedUser,
                                UserCredential userCredential) {
        User authUser = userCredential.getUser();
        authUser.setFirstName(editedUser.getFirstName());
        authUser.setLastName(editedUser.getLastName());
        authUser.setPhoneNumber(editedUser.getPhoneNumber());
        authUser.setCurrentPosition(editedUser.getCurrentPosition());
        authUser.setContactEmail(editedUser.getContactEmail() != null ? editedUser.getContactEmail() : userCredential.getEmail());
        authUser.setCountry(editedUser.getCountry());
        authUser.setCity(editedUser.getCity());
        userRepository.save(authUser);
    }

    // TODO: replace
    public void addSkills(List<Skill> skills,
                          UserCredential userCredential){
        User authUser = userCredential.getUser();
        skills.forEach(skill -> authUser.getSkills().add(skill));
        userRepository.save(authUser);
    }
}
