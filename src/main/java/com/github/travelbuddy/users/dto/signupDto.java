package com.github.travelbuddy.users.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class signupDto {
    private String name;
    private String email;
    private String password;
    private Integer residentNum;
    //프로필사진
}
