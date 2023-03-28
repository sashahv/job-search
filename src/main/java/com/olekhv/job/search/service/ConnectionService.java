package com.olekhv.job.search.service;

import com.olekhv.job.search.auth.userCredential.UserCredential;
import com.olekhv.job.search.auth.userCredential.UserCredentialRepository;
import com.olekhv.job.search.entity.user.User;
import com.olekhv.job.search.entity.connectionRequest.ConnectionRequest;
import com.olekhv.job.search.entity.connectionRequest.ConnectionRequestRepository;
import com.olekhv.job.search.exception.AlreadyExistsException;
import com.olekhv.job.search.exception.NotFoundException;
import com.olekhv.job.search.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConnectionService {
    private final UserCredentialRepository userCredentialRepository;
    private final UserRepository userRepository;
    private final ConnectionRequestRepository connectionRequestRepository;

    public List<ConnectionRequest> listAllConnectionRequests(UserCredential userCredential){
        return connectionRequestRepository.findByToUser(userCredential.getUser()).orElseThrow(
                () -> new NotFoundException("No connection requests yet")
        );
    }

    public List<User> listAllConnectionsOfUser(String userEmail){
        UserCredential userCredential = findUserCredentialByEmail(userEmail);
        User user = userCredential.getUser();
        return user.getConnections();
    }

    public ConnectionRequest sendConnectionRequestToUser(String userEmail,
                                            UserCredential userCredential) {
        User authUser = userCredential.getUser();
        UserCredential userCredentialToSendRequest = findUserCredentialByEmail(userEmail);
        User userToRequest = userCredentialToSendRequest.getUser();
        if(connectionRequestRepository.findByFromUserAndToUser(authUser, userToRequest).isPresent()){
            throw new AlreadyExistsException("Connection request to "
                    + userToRequest.getFirstName()  + " " + userToRequest.getLastName()
                    + " had been already sent");
        }
        ConnectionRequest connectionRequest = ConnectionRequest.builder()
                .fromUser(authUser)
                .toUser(userToRequest)
                .build();
        connectionRequestRepository.save(connectionRequest);
        return connectionRequest;
    }

    public List<ConnectionRequest> acceptConnectionRequest(String userEmail,
                                                           UserCredential userCredential){
        User authUser = userCredential.getUser();
        UserCredential sentRequestUserCredential = findUserCredentialByEmail(userEmail);
        User sentRequestUser = sentRequestUserCredential.getUser();
        ConnectionRequest connectionRequest = findConnectionByFromUserToUser(authUser, sentRequestUser);
        sentRequestUser.getConnections().add(authUser);
        authUser.getConnections().add(sentRequestUser);
        userRepository.save(authUser);
        userRepository.save(sentRequestUser);
        connectionRequestRepository.delete(connectionRequest);
        return connectionRequestRepository.findByToUser(authUser).get();
    }

    public List<ConnectionRequest> declineConnectionRequest(String userEmail,
                                               UserCredential userCredential){
        User authUser = userCredential.getUser();
        UserCredential sentRequestUserCredential = findUserCredentialByEmail(userEmail);
        User sentRequestUser = sentRequestUserCredential.getUser();
        ConnectionRequest connectionRequest = findConnectionByFromUserToUser(authUser, sentRequestUser);
        connectionRequestRepository.delete(connectionRequest);
        return connectionRequestRepository.findByToUser(authUser).get();
    }

    @Transactional
    public List<User> removeConnectionWithOtherUser(String userEmail,
                                                    UserCredential userCredential){
        User authUser = userCredential.getUser();
        UserCredential connectedUserCredential = findUserCredentialByEmail(userEmail);
        User connectedUser = connectedUserCredential.getUser();
        if(!authUser.getConnections().contains(connectedUser)){
            throw new NotFoundException("Not found connection with "
                    + connectedUser.getFirstName() + " " + connectedUser.getLastName());
        }
        connectedUser.getConnections().remove(authUser);
        authUser.getConnections().remove(connectedUser);
        userRepository.save(authUser);
        userRepository.save(connectedUser);
        return authUser.getConnections();
    }

    private UserCredential findUserCredentialByEmail(String userEmail) {
        return userCredentialRepository.findByEmail(userEmail).orElseThrow(
                () -> new NotFoundException("User with email " + userEmail + " not found")
        );
    }

    private ConnectionRequest findConnectionByFromUserToUser(User authUser, User sentRequestUser) {
        return connectionRequestRepository.findByFromUserAndToUser(sentRequestUser, authUser).orElseThrow(
                () -> new NotFoundException("Connection request not found")
        );
    }
}
