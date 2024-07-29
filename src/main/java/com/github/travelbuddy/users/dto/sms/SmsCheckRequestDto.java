package com.github.travelbuddy.users.dto.sms;

import lombok.Getter;

@Getter
public class SmsCheckRequestDto {
    private String phoneNum;
    private String code;
}
