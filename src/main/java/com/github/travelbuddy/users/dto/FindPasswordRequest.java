package com.github.travelbuddy.users.dto;

import lombok.Getter;

@Getter
public class FindPasswordRequest {
    private String email;
    private String phoneNum;
}
