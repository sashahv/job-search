package com.olekhv.job.search.service;

import com.olekhv.job.search.auth.userCredential.UserCredential;
import com.olekhv.job.search.auth.userCredential.UserCredentialRepository;
import com.olekhv.job.search.entity.user.User;
import com.olekhv.job.search.entity.connectionRequest.ConnectionRequest;
import com.olekhv.job.search.entity.connectionRequest.ConnectionRequestRepository;
import com.olekhv.job.search.exception.AlreadyExistsException;
import com.olekhv.job.search.exception.NotFoundException;
import com.olekhv.job.search.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConnectionRequestService {
    private final UserCredentialRepository userCredentialRepository;
    private final UserRepository userRepository;
    private final ConnectionRequestRepository connectionRequestRepository;

    public List<ConnectionRequest> listAllConnectionRequests(UserCredential userCredential){
        return connectionRequestRepository.findByToUser(userCredential.getUser()).orElseThrow(
                () -> new NotFoundException("No connection requests yet")
        );
    }

    public ConnectionRequest sendConnectionRequestToUser(String userEmail,
                                            UserCredential userCredential) {

        User authUser = userCredential.getUser();

        UserCredential userCredentialToSendRequest = userCredentialRepository.findByEmail(userEmail).orElseThrow(
                () -> new UsernameNotFoundException("User with email " + userEmail + " does not exist")
        );

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

        return connectionRequestRepository.save(connectionRequest);
    }

    public List<ConnectionRequest> acceptConnectionRequest(String userEmail,
                                                           UserCredential userCredential){
        User authUser = userCredential.getUser();

        UserCredential sentRequestUserCredential = userCredentialRepository.findByEmail(userEmail).orElseThrow(
                () -> new NotFoundException("User with email " + userEmail + " not found")
        );

        User sentRequestUser = sentRequestUserCredential.getUser();

        ConnectionRequest connectionRequest = connectionRequestRepository.findByFromUserAndToUser(sentRequestUser, authUser).orElseThrow(
                () -> new NotFoundException("Connection request not found")
        );

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

        UserCredential sentRequestUserCredential = userCredentialRepository.findByEmail(userEmail).orElseThrow(
                () -> new NotFoundException("User with email " + userEmail + " not found")
        );

        User sentRequestUser = sentRequestUserCredential.getUser();

        ConnectionRequest connectionRequest = connectionRequestRepository.findByFromUserAndToUser(sentRequestUser, authUser).orElseThrow(
                () -> new NotFoundException("Connection request not found")
        );

        connectionRequestRepository.delete(connectionRequest);
        return connectionRequestRepository.findByToUser(authUser).get();
    }
}
