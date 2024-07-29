package com.github.travelbuddy.common.controller.chat;

import com.github.travelbuddy.chat.dto.ChatRoomDto;
import com.github.travelbuddy.chat.entity.ChatMessage;
import com.github.travelbuddy.chat.dto.ChatNotification;
import com.github.travelbuddy.chat.entity.ChatRoom;
import com.github.travelbuddy.chat.service.ChatMessageService;
import com.github.travelbuddy.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;

    @MessageMapping("/chat/enter")
    public void processMessage(@Payload ChatMessage chatMessage) {
        log.info("=================== /publish/chat ===================");

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

    @GetMapping("/api/chat/room/enter")
    public ResponseEntity<List<ChatRoom>> findChatRoom(@RequestBody ChatRoomDto chatRoomDto) {
        log.info("============= FIND CHAT ROOM ===============");
        List<ChatRoom> chatRoomList = new ArrayList<>();
        return ResponseEntity.status(HttpStatus.OK).body(chatRoomList);
//        return ResponseEntity.ok(chatRoomService.getChatRoomId(chatRoomDto.getMyId(), chatRoomDto.getOpponentId(), true));
    }

    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessage>> findChatMessage(
            @PathVariable("senderId") String senderId,
            @PathVariable("recipientId") String recipientId
    ) {
        return ResponseEntity.ok(chatMessageService.findChatMessages(senderId, recipientId));
    }
}
