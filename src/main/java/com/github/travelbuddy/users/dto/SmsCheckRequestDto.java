package com.github.travelbuddy.users.dto;

import lombok.Getter;

@Getter
public class SmsCheckRequestDto {
    private String phoneNum;
    private String code;
}
