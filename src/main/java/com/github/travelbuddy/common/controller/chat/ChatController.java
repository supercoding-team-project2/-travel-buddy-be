package com.github.travelbuddy.common.controller.chat;

import com.github.travelbuddy.chat.dto.ChatMessageRequestDto;
import com.github.travelbuddy.chat.dto.ChatMessageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
//    private final SimpMessageSendingOperations messagingTemplate;

    @ResponseBody
    @MessageMapping("/chats")
    public void message(ChatMessageRequestDto chatMessageRequestDto) {
        System.out.println("============================= /chat ====================================");
        ChatMessageResponseDto chatMessageResponseDto = new ChatMessageResponseDto();
    }
}
