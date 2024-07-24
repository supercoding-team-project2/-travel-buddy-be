package com.github.travelbuddy.common.controller;

import com.github.travelbuddy.users.dto.*;
import com.github.travelbuddy.users.service.MessageService;
import com.github.travelbuddy.users.service.UserService;
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
        log.info("회원가입 전 문자인증 코드 발송");
        return messageService.sendSms(request.getPhoneNum());
    }

    @PostMapping("/signup/sms/check")
    public ResponseEntity<UserResponse> checkSmsAndUser(@RequestBody SmsCheckRequestDto request){
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
        return userService.findPassword(request.getEmail(), request.getPhoneNum());
    }

    //새 비밀번호 생성
    @PutMapping("/password/update")
    public ResponseEntity<UserResponse> updatePassword(@RequestBody UpdatePasswordRequest request){
        ResponseEntity<UserResponse> response = userService.updatePassword(request);
        return response;
    }
}
