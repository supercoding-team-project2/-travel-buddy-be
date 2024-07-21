package com.github.travelbuddy.users.service;
import com.github.travelbuddy.users.dto.UserResponse;
import com.github.travelbuddy.users.sms.SmsUtil;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.model.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final SmsUtil smsUtil;

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
        return ResponseEntity.ok(new UserResponse("인증번호: "+certificationNumber));
    }
}
