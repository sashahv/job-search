package com.olekhv.job.search.auth;

import com.olekhv.job.search.auth.userCredential.PasswordModel;
import com.olekhv.job.search.auth.userCredential.UserCredential;
import com.olekhv.job.search.auth.userCredential.UserCredentialRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@SpringBootTest
class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserCredential userCredential;

    @Mock
    private UserCredentialRepository userCredentialRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void should_change_user_password(){
        // Given
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";
        String passwordConfirmation = "newPassword";
        String encodedPassword = "encodedPassword";
        when(passwordEncoder.encode(newPassword)).thenReturn(encodedPassword);
        when(passwordEncoder.matches(oldPassword, userCredential.getPassword())).thenReturn(true);
        PasswordModel passwordModel = mock(PasswordModel.class);
        when(passwordModel.getOldPassword()).thenReturn(oldPassword);
        when(passwordModel.getNewPassword()).thenReturn(newPassword);
        when(passwordModel.getPasswordConfirmation()).thenReturn(passwordConfirmation);

        // When
        authenticationService.changePassword(passwordModel, userCredential);

        // Then
        verify(userCredential, times(1)).setPassword(encodedPassword);
        verify(userCredentialRepository, times(1)).save(userCredential);
    }
}