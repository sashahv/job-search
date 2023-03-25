package com.olekhv.job.search.service;

import com.olekhv.job.search.auth.userCredential.UserCredential;
import com.olekhv.job.search.entity.education.Education;
import com.olekhv.job.search.entity.user.User;
import com.olekhv.job.search.entity.user.WorkExperience;
import com.olekhv.job.search.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
class WorkExperienceServiceTest {

    @InjectMocks
    private WorkExperienceService workExperienceService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private User user;

    @Mock
    private UserCredential userCredential;

    @Mock
    private WorkExperience workExperience;

    @BeforeEach
    void setUp() {
        when(user.getWorkExperiences()).thenReturn(new ArrayList<>());
        when(userCredential.getUser()).thenReturn(user);
    }

    @Test
    public void should_add_new_education_for_user () {
        // Given
            // setUp()

        // When
        workExperienceService.addNewWorkExperience(workExperience, userCredential);

        // Then
        verify(userRepository, times(1)).save(user);
        assertThat(user.getWorkExperiences().contains(workExperience)).isTrue();
    }

}