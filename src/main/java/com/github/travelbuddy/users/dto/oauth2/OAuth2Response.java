package com.github.travelbuddy.users.dto.oauth2;


public interface OAuth2Response {

    //제공자
//    String getProvider();
//    String getProviderId();

    String getName();
    String getEmail();
    String getGender();
    String getBirthday();
    String getBirthYear();
    String getPhoneNumber();
}
