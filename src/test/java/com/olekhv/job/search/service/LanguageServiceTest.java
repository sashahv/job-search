package com.olekhv.job.search.service;

import com.olekhv.job.search.auth.userCredential.UserCredential;
import com.olekhv.job.search.entity.education.Education;
import com.olekhv.job.search.entity.language.Language;
import com.olekhv.job.search.entity.language.LanguageProficiency;
import com.olekhv.job.search.entity.language.UserLanguage;
import com.olekhv.job.search.entity.user.User;
import com.olekhv.job.search.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class LanguageServiceTest {

    @InjectMocks
    private LanguageService languageService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private User user;

    @Mock
    private UserCredential userCredential;

    @Mock
    private Language language;

    @Mock
    private UserLanguage userLanguage;

    @BeforeEach
    void setUp() {
        when(userCredential.getUser()).thenReturn(user);
        when(user.getLanguages()).thenReturn(new ArrayList<>());
        when(userLanguage.getLanguage()).thenReturn(language);
    }

    @Test
    void should_add_language_to_user(){
        // Given

        // When
        languageService.addLanguageToUser(userLanguage, userCredential);

        // Then
        verify(userRepository, times(1)).save(user);
        assertThat(user.getLanguages().contains(userLanguage)).isTrue();
    }
}