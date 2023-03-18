package com.olekhv.job.search.skill;

import com.olekhv.job.search.auth.userCredential.UserCredential;
import com.olekhv.job.search.user.User;
import com.olekhv.job.search.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    /* Find all skills by IDs in skillsId list,
    passes through them and
    if skill is not presented in skills in auth user
    it's added to that list*/
    public List<Skill> addNewSkills(List<Long> skillsId,
                                    UserCredential userCredential) {
        User authUser = userCredential.getUser();
        List<Skill> authUserSkills = authUser.getSkills();
        authUserSkills.addAll(
                skillsId.stream()
                .map(skillRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(skill -> !authUserSkills.contains(skill))
                .collect(Collectors.toList()));
        userRepository.save(authUser);
        return authUserSkills;
    }
}
