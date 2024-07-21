package com.github.travelbuddy.common.controller.chat;

import com.github.travelbuddy.chat.entity.ChatMessage;
import com.github.travelbuddy.chat.dto.ChatNotification;
import com.github.travelbuddy.chat.service.ChatMessageService;
import com.github.travelbuddy.users.jwt.JWTUtill;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;
    private final JWTUtill jwtUtill;
    private static final String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsImlhdCI6MTcyMTQwMTQ5NiwiZXhwIjoxNzIxNDE5NDk2fQ.0d10AIQoQ8gvrrsJH0h0xTHGhfWoX9aR2a_8noObWR4";

    @MessageMapping("/chat/enter")
    public void processMessage(@Payload ChatMessage chatMessage) {
        log.info("=================== /publish/chat ===================");
        Integer userId = jwtUtill.getUserId(token);
        log.info("userId: " + userId);

        ChatMessage savedMessage = chatMessageService.save(chatMessage);
        log.info("savedMessage = " + savedMessage);

        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipientName(), // user
                "queue/messages",             // destination
                ChatNotification.builder()    // payload
                        .id(String.valueOf(savedMessage.getId()))
                        .senderId(savedMessage.getSenderName())
                        .recipientId(savedMessage.getRecipientName())
                        .content(savedMessage.getContent())
                        .build()
        );
    }

    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessage>> findChatMessage(
            @PathVariable("senderId") String senderId,
            @PathVariable("recipientId") String recipientId
    ) {
        return ResponseEntity.ok(chatMessageService.findChatMessages(senderId, recipientId));
    }
}
