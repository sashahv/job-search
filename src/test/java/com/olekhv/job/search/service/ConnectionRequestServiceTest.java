package com.olekhv.job.search.service;

import com.olekhv.job.search.auth.userCredential.UserCredential;
import com.olekhv.job.search.auth.userCredential.UserCredentialRepository;
import com.olekhv.job.search.entity.connectionRequest.ConnectionRequestRepository;
import com.olekhv.job.search.entity.user.User;
import com.olekhv.job.search.entity.connectionRequest.ConnectionRequest;
import com.olekhv.job.search.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class ConnectionRequestServiceTest {

    @InjectMocks private ConnectionRequestService connectionRequestService;

    @Mock private ConnectionRequestRepository connectionRequestRepository;
    @Mock private UserRepository userRepository;
    @Mock private UserCredentialRepository userCredentialRepository;
    @Mock private User user;
    @Mock private UserCredential userCredential;
    @Mock private ConnectionRequest connectionRequest;
    @Mock private UserCredential requestedUserCredential;
    @Mock private User requestedUser;

    @BeforeEach
    void setUp() {
        when(userCredential.getUser()).thenReturn(user);
        when(user.getConnections()).thenReturn(new ArrayList<>());
        when(requestedUser.getConnections()).thenReturn(new ArrayList<>());
        when(requestedUserCredential.getUser()).thenReturn(requestedUser);
        when(userCredentialRepository.findByEmail("testUser@gmail.com")).thenReturn(Optional.of(requestedUserCredential));
    }

    @Test
    void should_send_connection_request(){
        // Given
        when(connectionRequestRepository.findByFromUserAndToUser(user, requestedUser)).thenReturn(Optional.empty());

        // When
        connectionRequestService.sendConnectionRequestToUser("testUser@gmail.com", userCredential);

        // Then
        verify(connectionRequestRepository, times(1)).save(connectionRequest);
    }

    @Test
    void should_accept_connection_request(){
        // Given
        when(connectionRequestRepository.findByToUser(user)).thenReturn(Optional.of(List.of(connectionRequest)));
        when(connectionRequestRepository.findByFromUserAndToUser(any(User.class), any(User.class))).thenReturn(Optional.of(connectionRequest));

        // When
        connectionRequestService.acceptConnectionRequest("testUser@gmail.com", userCredential);

        // Then
        verify(userRepository,times(1)).save(user);
        verify(userRepository,times(1)).save(requestedUser);
        verify(connectionRequestRepository,times(1)).delete(connectionRequest);
        assertEquals(1, user.getConnections().size());
        assertEquals(1, requestedUser.getConnections().size());
    }

    @Test
    void should_decline_connection_request(){
        // Given
        when(connectionRequestRepository.findByFromUserAndToUser(any(User.class), any(User.class))).thenReturn(Optional.of(connectionRequest));
        when(connectionRequestRepository.findByToUser(user)).thenReturn(Optional.of(List.of(connectionRequest)));

        // When
        connectionRequestService.declineConnectionRequest("testUser@gmail.com", userCredential);

        // Then
        verify(connectionRequestRepository,times(1)).delete(connectionRequest);
        assertEquals(0, user.getConnections().size());
        assertEquals(0, requestedUser.getConnections().size());
    }
}