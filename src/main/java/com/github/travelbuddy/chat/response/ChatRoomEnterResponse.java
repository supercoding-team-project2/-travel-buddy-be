package com.github.travelbuddy.chat.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ChatRoomEnterResponse {
    private String chatRoomId;
    private String message;
}
