package com.github.travelbuddy.common.controller;

import com.github.travelbuddy.users.dto.*;
import com.github.travelbuddy.users.service.MessageService;
import com.github.travelbuddy.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final MessageService messageService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(SignupDto signupDto){
        return userService.signup(signupDto);
    }


    @GetMapping("/")
    public ResponseEntity<UserInfoResponse> getUserInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        UserInfoResponse response = userService.getUserInfo(userDetails.getUserId());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/profile-picture")
    public ResponseEntity<UserResponse> updateUserInfo(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                       @RequestParam("profilePicture") MultipartFile profilePicture) {
        return userService.updateUserInfo(userDetails.getUserId(), profilePicture);
    }

    @PostMapping("/sms/send")
    public ResponseEntity<UserResponse> sendSms(@RequestBody SmsRequestDto request){
        //이미 가입한 유저인지 확인
        ResponseEntity<UserResponse> response = userService.checkUserExist(request.getPhoneNum());
        if(response == null){
            return messageService.sendSms(request.getPhoneNum());
        }else {
            return response;
        }
    }
}
