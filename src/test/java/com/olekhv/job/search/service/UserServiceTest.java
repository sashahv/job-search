package com.olekhv.job.search.service;

import com.olekhv.job.search.auth.userCredential.UserCredential;
import com.olekhv.job.search.auth.userCredential.UserCredentialRepository;
import com.olekhv.job.search.entity.user.User;
import com.olekhv.job.search.exception.NotFoundException;
import com.olekhv.job.search.repository.UserRepository;
import com.olekhv.job.search.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        when(userCredential.getUser()).thenReturn(user);
        when(userCredentialRepository.findByEmail("testEmail@gmail.com")).thenReturn(Optional.of(userCredential));
    }

    @Test
    void should_edit_user_information() {
        // Given
        User editedUser = new User();
        editedUser.setFirstName("ChangedName");

        // When
        userService.editInformation(editedUser, userCredential);

        // Then
        verify(user, times(1)).setFirstName("ChangedName");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void should_remove_connection_with_other_user(){
        // Given
        UserCredential connectedUserCredential = mock(UserCredential.class);
        User connectedUser = mock(User.class);
        when(connectedUserCredential.getUser()).thenReturn(connectedUser);
        when(user.getConnections()).thenReturn(new ArrayList<>(Collections.singletonList(connectedUser)));
        when(connectedUser.getConnections()).thenReturn(new ArrayList<>(Collections.singletonList(user)));
        when(userCredential.getUser()).thenReturn(user);
        when(userCredentialRepository.findByEmail("testConnectedEmail@gmail.com")).thenReturn(Optional.of(connectedUserCredential));

        // When
        userService.removeConnectionWithOtherUser("testConnectedEmail@gmail.com", userCredential);

        // Then
        verify(userRepository,times(2)).save(any(User.class));
        assertEquals(0, user.getConnections().size());
        assertEquals(0, connectedUser.getConnections().size());
    }

    @Test
    void should_throw_exception_if_connection_not_exists(){
        assertThrows(NotFoundException.class, () ->
                userService.removeConnectionWithOtherUser("testEmail@gmail.com", userCredential));
    }
}