package com.olekhv.job.search.auth;

import lombok.Builder;
import lombok.Data;

@Data
public class AuthorizationRequest {
    private String email;
    private String password;
}
