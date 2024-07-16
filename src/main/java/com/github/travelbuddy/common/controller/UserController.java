package com.github.travelbuddy.common.controller;

import com.github.travelbuddy.users.dto.SignupDto;
import com.github.travelbuddy.users.dto.SignupResponse;
import com.github.travelbuddy.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@RequestBody SignupDto signupDto){
        userService.signup(signupDto);
        return ResponseEntity.ok(new SignupResponse("회원가입 완료"));
    }
}
