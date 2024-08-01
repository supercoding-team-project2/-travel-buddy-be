package com.github.travelbuddy.chat.service;

import com.github.travelbuddy.chat.entity.ChatRoom;
import com.github.travelbuddy.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    public Optional<String> getChatRoomId(String senderId, String opponentId, boolean createNewRoomIfNotExists) {
        log.info("senderId = " + senderId);
        log.info("opponentId = " + opponentId);
        return chatRoomRepository.findBySenderIdAndRecipientId(senderId, opponentId)
                .map(ChatRoom::getChatId)
                .or(() -> {
                    if(createNewRoomIfNotExists) {
                        String chatId = createChatId(senderId, opponentId);
                        return Optional.of(chatId);
                    }
                    return Optional.empty();
                });
    }

    private String createChatId(String myId, String opponentId) {
        String chatRoomId = String.format("%s_%s", myId, opponentId);

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

    public List<ChatRoom> getAllChatRooms(Integer userId) {
        List<ChatRoom> allChatRoomsInDB = chatRoomRepository.findAll();
        log.info("allChatRoomsInDB.size(): " + allChatRoomsInDB.size());

        for(ChatRoom chatRoom : allChatRoomsInDB) {
            String chatId = chatRoom.getChatId();
            log.info("chatId : " + chatId);

        }

        return null;
    }
}
