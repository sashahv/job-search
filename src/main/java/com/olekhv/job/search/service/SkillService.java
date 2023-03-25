package com.olekhv.job.search.service;

import com.olekhv.job.search.auth.userCredential.UserCredential;
import com.olekhv.job.search.auth.userCredential.UserCredentialRepository;
import com.olekhv.job.search.entity.skill.Skill;
import com.olekhv.job.search.repository.SkillRepository;
import com.olekhv.job.search.entity.user.User;
import com.olekhv.job.search.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SkillService {
    private final SkillRepository skillRepository;
    private final UserRepository userRepository;
    private final UserCredentialRepository userCredentialRepository;

    public List<Skill> listAllSkillsOfUser(String userEmail){
        UserCredential userCredential = findUserCredentialByEmail(userEmail);
        User user = userCredential.getUser();
        return user.getSkills();
    }

    /* Find all skills by IDs in skillsId list,
    passes through them and
    if skill is not presented in auth user's skill list
    it's added to that list*/
    public List<Skill> addSkillsToUser(List<Long> skillsId,
                                       UserCredential userCredential) {
        User authUser = userCredential.getUser();
        List<Skill> userSkills = authUser.getSkills();
        userSkills.addAll(
                skillsId.stream()
                .map(skillRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(skill -> !userSkills.contains(skill))
                .collect(Collectors.toList()));
        userRepository.save(authUser);
        return userSkills;
    }

    private UserCredential findUserCredentialByEmail(String userEmail) {
        return userCredentialRepository.findByEmail(userEmail).orElseThrow(
                () -> new UsernameNotFoundException("User with email " + userEmail + " does not exist")
        );
    }
}
