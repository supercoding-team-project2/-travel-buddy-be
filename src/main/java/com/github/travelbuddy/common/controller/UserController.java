package com.github.travelbuddy.common.controller;

import com.github.travelbuddy.users.dto.*;
import com.github.travelbuddy.users.dto.sms.SmsCheckRequestDto;
import com.github.travelbuddy.users.dto.sms.SmsSendRequestDto;
import com.github.travelbuddy.users.service.MessageService;
import com.github.travelbuddy.users.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final MessageService messageService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(SignupDto signupDto){
        log.info("회원가입");
        try {
            return userService.signup(signupDto);
        }catch (IOException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<UserInfoResponse> getUserInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        UserInfoResponse response = userService.getUserInfo(userDetails.getUserId());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/profile-picture")
    public ResponseEntity<?> updateUserInfo(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                       @RequestParam("profilePicture") MultipartFile profilePicture) {
        try {
            return userService.updateUserInfo(userDetails.getUserId(), profilePicture);
        }catch (IOException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/signup/sms/send")
    public ResponseEntity<UserResponse> sendSms(@RequestBody SmsSendRequestDto request){
        log.info("회원가입 전 문자인증 코드 발송 /signup/sms/send");
        return messageService.sendSms(request.getPhoneNum());
    }

    @PostMapping("/signup/sms/check")
    public ResponseEntity<UserResponse> checkSmsAndUser(@RequestBody SmsCheckRequestDto request){
        log.info("회원 가입 전 문자인증 확인과 이미 가입한 유저인지 확인 phone={}, code={}",request.getPhoneNum(),request.getCode());
        //코드 맞는지 확인과 가입한 유저인지 확인
        Boolean isValid = messageService.checkSmsCode(request.getPhoneNum(),request.getCode());
        if(isValid){
            //코드가 맞으면 유저있는지 없는지 확인
            return userService.checkUserExist(request.getPhoneNum());
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new UserResponse("인증 실패"));
        }
    }

    // 인증번호 확인
    @PostMapping("/sms-code/check")
    public ResponseEntity<UserResponse> checkSms(@RequestBody SmsCheckRequestDto request){
        log.info("/sms-code/check");
        Boolean isValid = messageService.checkSmsCode(request.getPhoneNum(),request.getCode());
        if(isValid){
            return ResponseEntity.ok(new UserResponse("인증 성공"));
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new UserResponse("인증 실패"));
        }
    }

    //비밀번호 찾기
    @PostMapping("/password/find")
    public ResponseEntity<UserResponse> findPassword(@RequestBody FindPasswordRequest request){
        log.info("/password/find");
        return userService.findPassword(request.getEmail(), request.getPhoneNum());
    }

    //새 비밀번호 생성
    @PutMapping("/password/update")
    public ResponseEntity<UserResponse> updatePassword(@RequestBody UpdatePasswordRequest request){
        log.info("/password/update");
        ResponseEntity<UserResponse> response = userService.updatePassword(request);
        return response;
    }

    //oAuth2 로그인시 쿠키로 jwt를 담아 보내는데 다시 헤더로 담아 보내기 위함
    @PostMapping("/oauth2-jwt-header")
    public ResponseEntity<?> oauth2JwtHeader(HttpServletRequest request, HttpServletResponse response){
        log.info("/oauth2-jwt-header");
        return userService.oauth2JwtHeader(request, response);
    }
}
