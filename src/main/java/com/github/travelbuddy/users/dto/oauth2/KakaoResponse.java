package com.github.travelbuddy.users.dto.oauth2;

import lombok.ToString;

import java.util.Map;

@ToString
public class KakaoResponse implements OAuth2Response{

    private final Map<String, Object> responseMap;

    public KakaoResponse(Map<String, Object> responseMap) {
        this.responseMap = (Map<String, Object>) responseMap.get("kakao_account");
    }

//    @Override
//    public String getProvider() {
//        return "kakao";
//    }
//
//    @Override
//    public String getProviderId() {
//        return attribute.get("id").toString();
//    }

    @Override
    public String getName() {
        Map<String, Object> profileMap = (Map<String, Object>) responseMap.get("profile");
        return profileMap != null && profileMap.get("nickname") != null ? profileMap.get("nickname").toString() : "";
//        return profileMap.get("nickname").toString();
    }

    @Override
    public String getEmail() {
        return responseMap != null && responseMap.get("email") != null ? responseMap.get("email").toString() : "";
//        return responseMap.get("email").toString();
    }

    @Override
    public String getGender() {
        return responseMap != null && responseMap.get("gender") != null ? responseMap.get("gender").toString() : "";
//        return responseMap.get("gender").toString();
    }

    @Override
    public String getBirthday() {
        return responseMap != null && responseMap.get("birthday") != null ? responseMap.get("birthday").toString() : "";
//        return responseMap.get("birthday").toString();
    }

    @Override
    public String getBirthYear() {
        return responseMap != null && responseMap.get("birthyear") != null ? responseMap.get("birthyear").toString() : "";
//        return responseMap.get("birthyear").toString();
    }

    @Override
    public String getPhoneNumber() {
        return responseMap != null && responseMap.get("phone_number") != null ? responseMap.get("phone_number").toString() : "";
//        return responseMap.get("phoneNumber").toString();
    }
}
