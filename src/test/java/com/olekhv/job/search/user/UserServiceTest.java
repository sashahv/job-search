package com.olekhv.job.search.user;

import com.olekhv.job.search.auth.userCredential.UserCredential;
import com.olekhv.job.search.auth.userCredential.UserCredentialRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserCredentialRepository userCredentialRepository;

    @Mock
    private User user;

    @Mock
    private UserCredential userCredential;

    @BeforeEach
    void setUp() {
        when(user.getFirstName()).thenReturn("Test");
        when(user.getLastName()).thenReturn("User");

        when(userCredential.getEmail()).thenReturn("testemail@gmail.com");
        when(userCredential.getUser()).thenReturn(user);

        when(userCredentialRepository.findByEmail("testemail@gmail.com")).thenReturn(Optional.of(userCredential));
    }

    @Test
    void should_edit_user_information() {
        User editedUser = new User();
        editedUser.setFirstName("ChangedName");

        userService.editInformation(editedUser, userCredential);

        verify(user, times(1)).setFirstName("ChangedName");
        verify(userRepository, times(1)).save(user);
    }
}