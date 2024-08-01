package com.github.travelbuddy.chat.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatNotification {
    private String roomId;
    private String senderId;
    private String opponentId;
    private String content;
    private String timeStamp;
}
