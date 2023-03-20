package com.olekhv.job.search.auth.userCredential;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PasswordModel {
    private String oldPassword;
    private String newPassword;
    private String passwordConfirmation;
}
