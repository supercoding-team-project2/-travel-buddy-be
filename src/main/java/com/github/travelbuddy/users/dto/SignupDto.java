package com.github.travelbuddy.users.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class SignupDto {
    private String name;
    private String email;
    private String password;
    private String residentNum;
    private String phoneNum;
    private MultipartFile profilePicture;
}
