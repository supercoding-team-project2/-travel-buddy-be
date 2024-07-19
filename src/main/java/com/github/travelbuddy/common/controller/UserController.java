package com.github.travelbuddy.common.controller;

import com.github.travelbuddy.users.dto.CustomUserDetails;
import com.github.travelbuddy.users.dto.SignupDto;
import com.github.travelbuddy.users.dto.SignupResponse;
import com.github.travelbuddy.users.dto.UserResponse;
import com.github.travelbuddy.users.jwt.JWTFilter;
import com.github.travelbuddy.users.repository.UserRepository;
import com.github.travelbuddy.users.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(SignupDto signupDto){
        return userService.signup(signupDto);
    }


    @GetMapping("/")
    public ResponseEntity<UserResponse> getUserInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        UserResponse response = userService.getUserInfo(userDetails.getUserId());
        return ResponseEntity.ok(response);
    }
}
