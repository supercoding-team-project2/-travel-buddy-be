package com.github.travelbuddy.users.dto;

import lombok.Getter;

@Getter
public class UpdatePasswordRequest {
    private String newPassword;
    private String email;
}
