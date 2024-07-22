package com.github.travelbuddy.users.service;
import com.github.travelbuddy.users.dto.UserResponse;
import com.github.travelbuddy.users.entity.SmsEntity;
import com.github.travelbuddy.users.repository.SmsRepository;
import com.github.travelbuddy.users.sms.SmsUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final SmsUtil smsUtil;
    private final SmsRepository smsRepository;

    private String createRandomNumber() {
        Random rand = new Random();
        String randomNum = "";
        for (int i = 0; i < 6; i++) {
            String random = Integer.toString(rand.nextInt(10));
            randomNum += random;
        }
        return randomNum;
    }

    public ResponseEntity<UserResponse> sendSms(String phoneNumber) {
        String certificationNumber = createRandomNumber();
        smsUtil.sendSms(phoneNumber, certificationNumber);

        //인증번호 db에 저장
        smsRepository.save(SmsEntity.builder()
                .phoneNum(phoneNumber)
                .code(certificationNumber)
                .build());

        return ResponseEntity.ok(new UserResponse("인증번호 전송"));
    }

    @Transactional
    public Boolean checkSmsCode(String phoneNum, String receivedCode) {
        String code = smsRepository.findByPhoneNum(phoneNum);
        if(code.equals(receivedCode) && code != null) {
            smsRepository.deleteByPhoneNum(phoneNum);
            return true;
        }else {
            return false;
        }
    }
}
