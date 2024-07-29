package com.github.travelbuddy.users.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.travelbuddy.users.dto.oauth2.CustomOAuth2User;
import com.github.travelbuddy.users.dto.oauth2.KakaoResponse;
import com.github.travelbuddy.users.dto.oauth2.OAuth2Response;
import com.github.travelbuddy.users.dto.oauth2.OAuth2UserDto;
import com.github.travelbuddy.users.entity.UserEntity;
import com.github.travelbuddy.users.enums.Gender;
import com.github.travelbuddy.users.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Value("${profile.url}")
    private String defaultProfileUrl;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{


        log.info("Oauth2UserService loadUser");
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String oauthClientName = userRequest.getClientRegistration().getClientName();

        OAuth2Response oauth2Response = null;
        if(oauthClientName.equals("kakao")){
            oauth2Response = new KakaoResponse(oAuth2User.getAttributes());
        }

        if (oauth2Response == null) {
            log.info("OAuth2Response is null");
            throw new OAuth2AuthenticationException("OAuth2 Response is null");
        }

        String phoneNum = oauth2Response.getPhoneNumber();
        String birthyear = oauth2Response.getBirthYear();
        String birthdate = oauth2Response.getBirthday();
        Gender gender = Gender.valueOf(oauth2Response.getGender().toUpperCase());

        String withoutCountryCode = phoneNum.replace("+82 ", "0").replace("-", "").trim();

        String birthyearAndDate = birthyear + birthdate;
        String residentNum = birthyearAndDate.substring(2);

        if(gender.equals(Gender.MALE)){
            if(residentNum.startsWith("0")){
                residentNum = residentNum +"3";
            }else residentNum = residentNum +"1";
        }else{
            if(residentNum.startsWith("0")){
                residentNum = residentNum +"4";
            }else residentNum = residentNum +"2";
        }

        UserEntity existData = userRepository.findByEmail(oauth2Response.getEmail());

        if(existData == null){
            log.info("소셜로그인 처음");
            UserEntity userEntity = UserEntity.builder()
                    .name(oauth2Response.getName())
                    .email(oauth2Response.getEmail())
                    .password("password")
                    .residentNum(residentNum)
                    .phoneNum(withoutCountryCode)
                    .gender(gender)
                    .createdAt(LocalDateTime.now())
                    .profilePictureUrl(defaultProfileUrl)
                    .build();
            userRepository.save(userEntity);

            OAuth2UserDto userDto = new OAuth2UserDto();
            userDto.setUserId(userEntity.getId());
            userDto.setName(oauth2Response.getName());
            userDto.setEmail(oauth2Response.getEmail());

            return new CustomOAuth2User(userDto);
        }else{
            log.info("소셜로그인 처음아님");
            UserEntity userEntity = existData.toBuilder()
                    .name(oauth2Response.getName())
                    .phoneNum(withoutCountryCode)
                    .build();
            userRepository.save(userEntity);

            OAuth2UserDto userDto = new OAuth2UserDto();
            userDto.setUserId(userEntity.getId());
            userDto.setName(oauth2Response.getName());
            userDto.setEmail(userEntity.getEmail());

            return new CustomOAuth2User(userDto);
        }
    }
}
