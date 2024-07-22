package com.github.travelbuddy.users.dto;

import lombok.Getter;

@Getter
public class UpdatePasswordRequest {
    private String newPassword;
    private String confirmPassword;
    private String email;
}
