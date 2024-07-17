package com.github.travelbuddy.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatMessageRequestDto {
    private Long chatRoomId;
    private String userEmail;
    private String message;
    private String imgCode; // 바이트 코드로 이미지 받음
}
