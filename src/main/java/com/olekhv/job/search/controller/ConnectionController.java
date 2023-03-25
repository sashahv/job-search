package com.olekhv.job.search.controller;

import com.olekhv.job.search.auth.userCredential.UserCredential;
import com.olekhv.job.search.entity.connectionRequest.ConnectionRequest;
import com.olekhv.job.search.entity.user.User;
import com.olekhv.job.search.service.ConnectionService;
import com.olekhv.job.search.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/connections")
public class ConnectionController {
    private final ConnectionService connectionService;

    @GetMapping("/all")
    public ResponseEntity<List<ConnectionRequest>> listAllConnectionRequests(@AuthenticationPrincipal UserCredential userCredential){
        return ResponseEntity.ok(connectionService.listAllConnectionRequests(userCredential));
    }

    @PostMapping
    public ResponseEntity<ConnectionRequest> sendConnectionRequest(@RequestParam String userEmail,
                                                                   @AuthenticationPrincipal UserCredential userCredential){
        return ResponseEntity.ok(connectionService.sendConnectionRequestToUser(userEmail, userCredential));
    }

    @PostMapping("/requests/accept")
    public ResponseEntity<List<ConnectionRequest>> acceptConnectionRequest(@RequestParam String userEmail,
                                                                           @AuthenticationPrincipal UserCredential userCredential){
        return ResponseEntity.ok(connectionService.acceptConnectionRequest(userEmail, userCredential));
    }

    @PostMapping("/requests/decline")
    public ResponseEntity<List<ConnectionRequest>> declineConnectionRequest(@RequestParam String userEmail,
                                                                            @AuthenticationPrincipal UserCredential userCredential){
        return ResponseEntity.ok(connectionService.declineConnectionRequest(userEmail, userCredential));
    }

    @DeleteMapping("/remove")
    public ResponseEntity<List<User>> removeConnection(@RequestParam String userEmail,
                                                       @AuthenticationPrincipal UserCredential userCredential){
        return ResponseEntity.ok(connectionService.removeConnectionWithOtherUser(userEmail, userCredential));
    }
}
