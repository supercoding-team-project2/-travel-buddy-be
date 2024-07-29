package com.github.travelbuddy.users.dto.oauth2;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OAuth2UserDto {
    private Integer userId;
    private String name;
    private String email;
}
