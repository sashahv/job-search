package com.olekhv.job.search.controller;

import com.olekhv.job.search.auth.userCredential.UserCredential;
import com.olekhv.job.search.entity.language.Language;
import com.olekhv.job.search.entity.user.User;
import com.olekhv.job.search.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @PutMapping("user/edit")
    public ResponseEntity<User> editInformation(@RequestBody User providedUser,
                                                @AuthenticationPrincipal UserCredential userCredential) {
        return ResponseEntity.ok(userService.editInformation(providedUser, userCredential));
    }
}
