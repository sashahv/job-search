package com.olekhv.job.search.auth;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RegisterRequest {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String phoneNumber;
    private String city;
    private String country;
    private String currentPosition;
    private String contactEmail;
}
