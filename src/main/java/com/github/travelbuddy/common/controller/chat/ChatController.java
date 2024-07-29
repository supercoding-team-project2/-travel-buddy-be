package com.github.travelbuddy.common.controller.chat;

import com.github.travelbuddy.chat.dto.ChatRoomEnterDto;
import com.github.travelbuddy.chat.entity.ChatMessage;
import com.github.travelbuddy.chat.dto.ChatNotification;
import com.github.travelbuddy.chat.response.ChatRoomOpenResponse;
import com.github.travelbuddy.chat.service.ChatMessageService;
import com.github.travelbuddy.chat.service.ChatRoomService;
import com.github.travelbuddy.users.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;

    @MessageMapping("/chat/send")
    public void processMessage(@Payload ChatMessage chatMessage) {
        log.info("=================== /publish/send ===================");

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

    @PostMapping("/api/chat/room/enter")
    public ResponseEntity<ChatRoomOpenResponse> findChatRoom(@RequestBody ChatRoomEnterDto chatRoomEnterDto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        log.info("============= FIND CHAT ROOM ===============");

        String myId = String.valueOf(chatRoomEnterDto.getMyId());
        String opponentId = String.valueOf(chatRoomEnterDto.getOpponentId());

        String chatRoomId = chatRoomService.getChatRoomId(myId, opponentId, true).get();
        log.info("chatRoomId = " + chatRoomId);

        return ResponseEntity.status(HttpStatus.OK).body(new ChatRoomOpenResponse(chatRoomId));
    }

    @PostMapping("/api/chat/room/{chatRoomId}")
    public ResponseEntity<?> getChatRoomData(@PathVariable String chatRoomId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        log.info("============= GET CHAT ROOM DATA ===============");
        String opponentId = chatRoomId.split("-")[1];
        log.info("opponentId = " + opponentId);

        log.info("chatRoomId = " + chatRoomId);

        return ResponseEntity.status(HttpStatus.OK).body(new ChatRoomOpenResponse(chatRoomId));
    }

    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessage>> findChatMessage(
            @PathVariable("senderId") String senderId,
            @PathVariable("recipientId") String recipientId
    ) {
        return ResponseEntity.ok(chatMessageService.findChatMessages(senderId, recipientId));
    }
}
