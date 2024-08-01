package com.github.travelbuddy.common.controller;

import com.github.travelbuddy.chat.dto.ChatRoomEnterDto;
import com.github.travelbuddy.chat.entity.ChatMessage;
import com.github.travelbuddy.chat.dto.ChatNotification;
import com.github.travelbuddy.chat.entity.ChatRoom;
import com.github.travelbuddy.chat.response.ChatRoomFindResponse;
import com.github.travelbuddy.chat.response.ChatRoomOpenResponse;
import com.github.travelbuddy.chat.service.ChatMessageService;
import com.github.travelbuddy.chat.service.ChatRoomService;
import com.github.travelbuddy.users.dto.CustomUserDetails;
import com.github.travelbuddy.users.entity.UserEntity;
import com.github.travelbuddy.users.repository.UserRepository;
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

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;
    private final UserRepository userRepository;

    @PostMapping("/api/chat/room/enter")
    public ResponseEntity<?> enterChatRoom(@RequestBody ChatRoomEnterDto chatRoomEnterDto) {
        String senderId = String.valueOf(chatRoomEnterDto.getSenderId());
        String opponentId = String.valueOf(chatRoomEnterDto.getOpponentId());

        String chatRoomId = chatRoomService.getChatRoomId(senderId, opponentId, true).get();
        ChatRoomFindResponse chatRoomFindResponse = new ChatRoomFindResponse();
        chatRoomFindResponse.setChatRoomId(chatRoomId);
        chatRoomFindResponse.setMessage("SUCCESS");

        return ResponseEntity.status(HttpStatus.OK).body(chatRoomFindResponse);
    }

    @GetMapping("/api/chat/room/{chatId}")
    public ResponseEntity<?> getChatRoomData(@PathVariable String chatId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        String senderId = String.valueOf(userDetails.getUserId());
        String opponentId = null;
        String idx0 = chatId.split("_")[0];
        String idx1 = chatId.split("_")[1];

        if(senderId.equals(idx0)) {
            opponentId = chatId.split("_")[1];
        }else if(senderId.equals(idx1)) {
            opponentId = chatId.split("_")[0];
        }

        UserEntity opponentUserEntity = userRepository.findById(Integer.valueOf(opponentId)).orElseThrow(() -> new RuntimeException("존재하지 않는 상대방입니다."));
        String opponentName = opponentUserEntity.getName();
        String opponentProfile = opponentUserEntity.getProfilePictureUrl();

        List<ChatMessage> chatMessages = chatMessageService.findChatMessages(chatId);

        ChatRoomOpenResponse chatRoomOpenResponse = ChatRoomOpenResponse.builder()
                .senderId(senderId)
                .opponentName(opponentName)
                .opponentId(opponentId)
                .opponentProfile(opponentProfile)
                .messages(chatMessages)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(chatRoomOpenResponse);
    }

    @MessageMapping("/chat/send")
    public void processMessage(@Payload ChatMessage chatMessage) {
        ChatMessage savedMessage = chatMessageService.save(chatMessage);
        String chatId = chatMessage.getChatId();

        String chatRoomId = chatMessage.getChatId();
        String destination = "/subscribe/" + chatRoomId + "/queue/messages";
        messagingTemplate.convertAndSend(
                destination,
                ChatNotification.builder()
                        .roomId(String.valueOf(savedMessage.getId()))
                        .senderId(savedMessage.getSenderId())
                        .opponentId(savedMessage.getOpponentId())
                        .content(savedMessage.getContent())
                        .timeStamp(savedMessage.getTimeStamp())
                        .build()
        );
    }

    @GetMapping("/api/chatRooms")
    public ResponseEntity<?> getAllChatRooms(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Integer userId = userDetails.getUserId();
        List<ChatRoom> chatRooms = chatRoomService.getAllChatRooms(userId);

        return ResponseEntity.status(HttpStatus.OK).body(chatRooms);
    }
}
