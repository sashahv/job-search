package com.olekhv.job.search.service;

import com.olekhv.job.search.auth.userCredential.UserCredential;
import com.olekhv.job.search.entity.language.Language;
import com.olekhv.job.search.entity.language.UserLanguage;
import com.olekhv.job.search.entity.skill.Skill;
import com.olekhv.job.search.entity.user.User;
import com.olekhv.job.search.repository.SkillRepository;
import com.olekhv.job.search.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class SkillServiceTest {

    @InjectMocks private SkillService skillService;

    @Mock private UserRepository userRepository;

    @Mock private User user;

    @Mock private UserCredential userCredential;

    @Mock private Skill skill;
    @Mock
    private SkillRepository skillRepository;

    @BeforeEach
    void setUp() {
        when(userCredential.getUser()).thenReturn(user);
        when(user.getLanguages()).thenReturn(new ArrayList<>());
    }

    @Test
    void should_add_language_to_user(){
        // Given
        when(skillRepository.findById(any(Long.class))).thenReturn(Optional.of(skill));
        List<Skill> skills = new ArrayList<>(Collections.singletonList(new Skill()));
        when(user.getSkills()).thenReturn(skills);

        // When
        skillService.addSkillsToUser(new ArrayList<>(), userCredential);

        // Then
        verify(userRepository, times(1)).save(user);
        assertThat(user.getSkills().size()).isEqualTo(1);
    }

}