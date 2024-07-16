package com.github.travelbuddy.users.service;

import com.github.travelbuddy.users.dto.SignupDto;
import com.github.travelbuddy.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void signup(SignupDto signupDto) {

    }
}
