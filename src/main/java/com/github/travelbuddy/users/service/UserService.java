package com.github.travelbuddy.users.service;

import com.github.travelbuddy.users.dto.SignupDto;
import com.github.travelbuddy.users.dto.SignupResponse;
import com.github.travelbuddy.users.entity.UserEntity;
import com.github.travelbuddy.users.enums.Gender;
import com.github.travelbuddy.users.enums.Role;
import com.github.travelbuddy.users.enums.Status;
import com.github.travelbuddy.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public ResponseEntity<SignupResponse> signup(SignupDto signupDto) {

        String email = signupDto.getEmail();
        String password = signupDto.getPassword();
        Boolean isExist = userRepository.existsByEmail(email);
        if(isExist){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new SignupResponse("이미 존재하는 이메일입니다."));
        }

        Integer residentNum = signupDto.getResidentNum()%10;
        Gender gender;
        if(residentNum == 1|| residentNum == 3){
            gender=Gender.MALE;
        }
        else if(residentNum == 2|| residentNum == 4){
            gender=Gender.FEMALE;
        }
        else{
            throw new IllegalArgumentException("유효하지 않은 주민등록번호입니다.");
        }
        log.info("gender={}",gender);

        UserEntity userEntity = UserEntity.builder()
                .name(signupDto.getName())
                .email(email)
                .password(bCryptPasswordEncoder.encode(password))
                .residentNum(signupDto.getResidentNum())
                .gender(gender)
                .status(Status.ACTIVE)
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(userEntity);
        return ResponseEntity.ok(new SignupResponse("회원가입 완료"));
    }
}
