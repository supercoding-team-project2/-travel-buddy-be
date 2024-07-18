package com.github.travelbuddy.users.dto;

import com.github.travelbuddy.users.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserResponse {
    private String email;
    private String name;
    //private String password;
    private String residentNum;
    private Gender gender;
    //프로필사진
}
