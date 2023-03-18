package com.olekhv.job.search.user;

import com.olekhv.job.search.auth.userCredential.UserCredential;
import com.olekhv.job.search.education.EducationService;
import com.olekhv.job.search.education.Education;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
class EducationServiceTest {

    @InjectMocks
    private EducationService educationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private User user;

    @Mock
    private UserCredential userCredential;

    @Mock
    private Education education;

    @BeforeEach
    void setUp() {
    }

    @Test
    public void should_add_new_education_for_user () {
        when(user.getEducations()).thenReturn(new ArrayList<>());
        when(userCredential.getUser()).thenReturn(user);

        educationService.addNewEducation(education, userCredential);

        verify(userRepository, times(1)).save(user);
        assertThat(user.getEducations().contains(education)).isTrue();
    }
}