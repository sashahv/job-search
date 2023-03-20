//package com.olekhv.job.search.service;
//
//import com.olekhv.job.search.auth.userCredential.UserCredential;
//import com.olekhv.job.search.auth.userCredential.UserCredentialRepository;
//import com.olekhv.job.search.entity.connectionRequest.ConnectionRequestRepository;
//import com.olekhv.job.search.entity.user.User;
//import com.olekhv.job.search.entity.connectionRequest.ConnectionRequest;
//import com.olekhv.job.search.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Optional;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest
//class ConnectionRequestServiceTest {
//
//    @InjectMocks
//    private ConnectionRequestService connectionRequestService;
//
//    @Mock
//    private ConnectionRequestRepository connectionRequestRepository;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private UserCredentialRepository userCredentialRepository;
//
//    @Mock
//    private User user;
//
//    @Mock
//    private UserCredential userCredential;
//
//    @Mock
//    private ConnectionRequest connectionRequest;
//
//    @BeforeEach
//    void setUp() {
//        when(userCredential.getUser()).thenReturn(user);
//    }
//
//    @Test
//    void should_send_connection_request(){
//        UserCredential userCredentialToSendRequest = mock(UserCredential.class);
//        User userToRequest = mock(User.class);
//        when(userCredentialRepository.findByEmail("testUser@gmail.com")).thenReturn(Optional.of(userCredentialToSendRequest));
//        when(userCredentialToSendRequest.getUser()).thenReturn(userToRequest);
//        when(userToRequest.getConnectionRequests()).thenReturn(new ArrayList<>());
//
//        connectionRequestService.sendConnectionRequestToUser("testUser@gmail.com", userCredential);
//
//        verify(userRepository, times(1)).save(userToRequest);
//        assertEquals(1, userToRequest.getConnectionRequests().size());
//        assertEquals(user, userToRequest.getConnectionRequests().get(0).getUserSentRequest());
//    }
//
//    @Test
//    void should_accept_connection_request(){
//        User requestedUser = mock(User.class);
//        when(user.getConnections()).thenReturn(new ArrayList<>());
//        when(connectionRequest.getUserSentRequest()).thenReturn(requestedUser);
//        when(requestedUser.getConnections()).thenReturn(new ArrayList<>());
//        when(connectionRequestRepository.findById(1L)).thenReturn(Optional.of(connectionRequest));
//
//        connectionRequestService.acceptConnectionRequest(1L, userCredential);
//
//        verify(userRepository,times(1)).save(user);
//        verify(userRepository,times(1)).save(requestedUser);
//        assertEquals(0, requestedUser.getConnectionRequests().size());
//        assertEquals(1, user.getConnections().size());
//        assertEquals(1, requestedUser.getConnections().size());
//    }
//
//    @Test
//    void should_decline_connection_request(){
//        User requestedUser = mock(User.class);
//        when(connectionRequest.getUserSentRequest()).thenReturn(requestedUser);
//        when(connectionRequestRepository.findById(1L)).thenReturn(Optional.of(connectionRequest));
//
//        connectionRequestService.declineConnectionRequest(1L, userCredential);
//
//        verify(userRepository,times(1)).save(requestedUser);
//        assertEquals(0, requestedUser.getConnectionRequests().size());
//        assertEquals(0, user.getConnections().size());
//        assertEquals(0, requestedUser.getConnections().size());
//    }
//}