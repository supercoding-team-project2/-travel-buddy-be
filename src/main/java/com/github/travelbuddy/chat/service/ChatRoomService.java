package com.github.travelbuddy.chat.service;

import com.github.travelbuddy.chat.entity.ChatRoom;
import com.github.travelbuddy.chat.repository.ChatRoomRepository;
import com.github.travelbuddy.common.util.UUIDUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    public Optional<String> getChatRoomId(String senderId, String recipientId, boolean createNewRoomIfNotExists) {
        log.info("senderId = " + senderId);
        log.info("recipientId = " + recipientId);
        return chatRoomRepository.findBySenderIdAndRecipientId(senderId, recipientId)
                .map(ChatRoom::getChatId)
                .or(() -> {
                    if(createNewRoomIfNotExists) {
                        String chatId = createChatId(senderId, recipientId);
                        return Optional.of(chatId);
                    }
                    return Optional.empty();
                });
    }

    private String createChatId(String myId, String opponentId) {
        StringBuilder sb = new StringBuilder();
        sb.append(myId);
        sb.append("_");
        sb.append(opponentId);
        sb.append("_");
        sb.append(UUIDUtil.generateUUIDFromUserId(Integer.valueOf(myId)));
        String chatRoomId = sb.toString();

        ChatRoom senderRecipient = ChatRoom.builder()
                .chatId(chatRoomId)
                .senderId(myId)
                .recipientId(opponentId)
                .build();

        ChatRoom recipientSender = ChatRoom.builder()
                .chatId(chatRoomId)
                .senderId(opponentId)
                .recipientId(myId)
                .build();

        chatRoomRepository.save(senderRecipient);
        chatRoomRepository.save(recipientSender);

        return chatRoomId;
    }
}
