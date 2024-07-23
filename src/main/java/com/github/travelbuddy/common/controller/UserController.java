package com.github.travelbuddy.common.controller;

import com.github.travelbuddy.users.dto.*;
import com.github.travelbuddy.users.service.MessageService;
import com.github.travelbuddy.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final MessageService messageService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(SignupDto signupDto){
        try {
            return userService.signup(signupDto);
        }catch (IOException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/")
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
        //이미 가입한 유저인지 확인
        ResponseEntity<UserResponse> response = userService.checkUserExist(request.getPhoneNum());
        if(response == null){
            return messageService.sendSms(request.getPhoneNum());
        }else {
            return response;
        }
    }

    //인증번호 확인
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
    //TODO: request수정
    @PostMapping("/password/find")
    public ResponseEntity<UserResponse> findPassword(@RequestBody FindPasswordRequest request){
        ResponseEntity<UserResponse> response = userService.findPassword(request.getEmail());
        if(response == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new UserResponse("가입되지 않은 emil입니다."));
        }else {
            return response;
        }
    }

    //새 비밀번호 생성
    @PutMapping("/password/update")
    public ResponseEntity<UserResponse> updatePassword(@RequestBody UpdatePasswordRequest request){
        ResponseEntity<UserResponse> response = userService.updatePassword(request);
        return response;
    }
}
