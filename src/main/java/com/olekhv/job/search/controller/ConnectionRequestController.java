package com.olekhv.job.search.controller;

import com.olekhv.job.search.auth.userCredential.UserCredential;
import com.olekhv.job.search.entity.connectionRequest.ConnectionRequest;
import com.olekhv.job.search.entity.user.User;
import com.olekhv.job.search.service.ConnectionRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/connections")
public class ConnectionRequestController {
    private final ConnectionRequestService connectionRequestService;

    @GetMapping("/all")
    public ResponseEntity<List<ConnectionRequest>> listAllConnectionRequests(@AuthenticationPrincipal UserCredential userCredential){
        return ResponseEntity.ok(connectionRequestService.listAllConnectionRequests(userCredential));
    }

    @PostMapping("/add")
    public ResponseEntity<ConnectionRequest> sendConnectionRequest(@RequestParam("email") String userEmail,
                                                      @AuthenticationPrincipal UserCredential userCredential){
        return ResponseEntity.ok(connectionRequestService.sendConnectionRequestToUser(userEmail, userCredential));
    }

    @PostMapping("/requests/accept")
    public ResponseEntity<List<ConnectionRequest>> acceptConnectionRequest(@RequestParam("email") String userEmail,
                                                                           @AuthenticationPrincipal UserCredential userCredential){
        return ResponseEntity.ok(connectionRequestService.acceptConnectionRequest(userEmail, userCredential));
    }

//    @PostMapping("/requests/decline")
//    public ResponseEntity<List<ConnectionRequest>> declineConnectionRequest(@RequestParam("id") Long id,
//                                                                            @AuthenticationPrincipal UserCredential userCredential){
//        return ResponseEntity.ok(connectionRequestService.declineConnectionRequest(id, userCredential));
//    }
}
