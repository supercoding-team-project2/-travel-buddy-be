package com.github.travelbuddy.chat.response;

import com.github.travelbuddy.chat.entity.ChatMessage;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomOpenResponse {
    private String senderId;
    private String opponentName;
    private String opponentId;
    private String opponentProfile;
    private List<ChatMessage> messages;
}
