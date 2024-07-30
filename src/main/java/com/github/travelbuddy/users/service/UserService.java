package com.github.travelbuddy.users.service;

import com.github.travelbuddy.common.service.S3Service;
import com.github.travelbuddy.users.dto.SignupDto;
import com.github.travelbuddy.users.dto.UpdatePasswordRequest;
import com.github.travelbuddy.users.dto.UserResponse;
import com.github.travelbuddy.users.dto.UserInfoResponse;
import com.github.travelbuddy.users.entity.UserEntity;
import com.github.travelbuddy.users.enums.Gender;
import com.github.travelbuddy.users.jwt.JWTUtill;
import com.github.travelbuddy.users.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MessageService messageService;
    private final S3Service s3Service;
    private final JWTUtill jwtUtill;

    @Value("${profile.url}")
    private String defaultProfileUrl;

    public ResponseEntity<UserResponse> signup(SignupDto signupDto) throws IOException {

        String email = signupDto.getEmail();
        String password = signupDto.getPassword();
        Boolean isExist = userRepository.existsByEmail(email);
        if(isExist){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new UserResponse("이미 존재하는 이메일입니다."));
        }

        String residentNum = signupDto.getResidentNum();
        Gender gender;
        char genderChar = residentNum.charAt(6);

        if(genderChar == '1'|| genderChar == '3'){
            gender=Gender.MALE;
        }
        else if(genderChar == '2'|| genderChar == '4'){
            gender=Gender.FEMALE;
        }
        else{
            throw new IllegalArgumentException("유효하지 않은 주민등록번호입니다.");
        }
        log.info("gender={}",gender);

        String pictureUrl = defaultProfileUrl;

        if(signupDto.getProfilePicture() != null){
            pictureUrl = s3Service.uploadFile(signupDto.getProfilePicture());
        }

        UserEntity userEntity = UserEntity.builder()
                .name(signupDto.getName())
                .email(email)
                .password(bCryptPasswordEncoder.encode(password))
                .residentNum(signupDto.getResidentNum())
                .phoneNum(signupDto.getPhoneNum())
                .gender(gender)
                .createdAt(LocalDateTime.now())
                .profilePictureUrl(pictureUrl)
                .build();

        userRepository.save(userEntity);
        return ResponseEntity.ok(new UserResponse("회원가입 완료되었습니다."));
    }

    public UserInfoResponse getUserInfo(Integer userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("정보조회할 해당 ID: " + userId + "를 찾을 수 없습니다."));

        return UserInfoResponse.builder()
                .email(userEntity.getEmail())
                .name(userEntity.getName())
                .residentNum(userEntity.getResidentNum())
                .gender(userEntity.getGender())
                .profilePictureUrl(userEntity.getProfilePictureUrl())
                .build();
    }

    @Transactional
    public ResponseEntity<?> updateUserInfo(Integer userId, MultipartFile profilePicture )throws IOException {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("정보수정할 해당 ID: "+ userId +"를 찾을 수 없습니다."));

        if(!userEntity.getProfilePictureUrl().equals(defaultProfileUrl)){
            s3Service.deleteFile(userEntity.getProfilePictureUrl());
        }
        String pictureUrl = s3Service.uploadFile(profilePicture);

        UserEntity updateUser = userEntity.toBuilder()
                .profilePictureUrl(pictureUrl)
                .build();
        userRepository.save(updateUser);

        return ResponseEntity.ok(pictureUrl);
    }

    public ResponseEntity<UserResponse> checkUserExist(String phoneNum) {
        Boolean isExist = userRepository.existsByPhoneNum(phoneNum);
        log.info("이미 가입된 번호인지 확인 phoneNum={} isExist={}",phoneNum,isExist);
        if(isExist){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new UserResponse("이미 가입된 번호입니다."));
        }else {
            return ResponseEntity.ok(new UserResponse("가입 가능한 번호입니다. 계속해서 회원가입을 진행해 주세요."));
        }
    }

    public ResponseEntity<UserResponse> findPassword(String email, String phoneNum) {
        Boolean isExist = userRepository.existsByEmailAndPhoneNum(email,phoneNum);
        if(isExist){
            return messageService.sendSms(phoneNum);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new UserResponse("가입되어있지 않은 유저입니다."));
        }
    }

    public ResponseEntity<UserResponse> updatePassword(UpdatePasswordRequest request) {
        String newPassword = request.getNewPassword();
        String email = request.getEmail();

        UserEntity userEntity = userRepository.findByEmail(email);

        UserEntity updatedUserEntity = userEntity.toBuilder()
                .password(bCryptPasswordEncoder.encode(newPassword)).build();

        userRepository.save(updatedUserEntity);
        return ResponseEntity.ok(new UserResponse("비밀번호 변경이 완료되었습니다."));
    }

    public ResponseEntity<?> oauth2JwtHeader(HttpServletRequest request, HttpServletResponse response) {
        log.info("oauth2JwtHeader requestURI: {}",request.getRequestURI());
        log.info("oauth2JwtHeader requestMethod: {}",request.getMethod());
        log.info("oauth2JwtHeader headerName: {}",request.getHeader("Authorization"));
        Cookie[] cookies = request.getCookies();
        String token = null;

        if(cookies == null){
            log.info("cookie null");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("cookie null");
        }
        for(Cookie cookie : cookies){
            if(cookie.getName().equals("Authorization")){
                token = cookie.getValue();
                log.info("token: {}",token);
            }
        }

        if(token == null){
            log.info("token null");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("token null");
        }



        Cookie cookie = new Cookie("Authorization", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

//        response.addCookie(customSuccessHandler.createCookies("Authorization",null,0));
        response.addHeader("Authorization", "Bearer " + token);
        return ResponseEntity.status(HttpStatus.OK).body("token을 헤더에 담아 전달 완료");
    }

}
