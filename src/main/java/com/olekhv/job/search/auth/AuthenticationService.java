package com.olekhv.job.search.auth;

import com.olekhv.job.search.config.JwtService;
import com.olekhv.job.search.exception.UserAlreadyExistsException;
import com.olekhv.job.search.user.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserCredentialRepository userCredentialRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        String requestEmail = request.getEmail();
        if (userCredentialRepository.findByEmail(requestEmail).isPresent()) {
            throw new UserAlreadyExistsException("User with email " + requestEmail + " already exists");
        }
        User user = User
                .builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .city(request.getCity())
                .country(request.getCountry())
                .dateOfCreation(LocalDate.now())
                .build();
        userRepository.save(user);

        UserCredential userCredential = UserCredential.builder()
                .user(user)
                .email(requestEmail)
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.USER)
                .build();

        userCredentialRepository.save(userCredential);
        String token = jwtService.generateToken(userCredential);

        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }

    public AuthenticationResponse authorize(AuthorizationRequest request) {
        String requestEmail = request.getEmail();
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestEmail,
                            request.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new IllegalArgumentException("Invalid email or password");
        }
        UserCredential userCredential = userCredentialRepository.findByEmail(requestEmail).orElseThrow(
                () -> new UsernameNotFoundException("User " + requestEmail + " not found")
        );
        String token = jwtService.generateToken(userCredential);
        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }
}
