package com.github.travelbuddy.chat.service;

import com.github.travelbuddy.chat.entity.ChatRoom;
import com.github.travelbuddy.chat.repository.ChatRoomRepository;
import com.github.travelbuddy.chat.response.GetAllRoomsForUserResponse;
import com.github.travelbuddy.users.entity.UserEntity;
import com.github.travelbuddy.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    public Optional<String> getChatRoomId(String senderId, String opponentId, boolean createNewRoomIfNotExists) {
        return chatRoomRepository.findBySenderIdAndRecipientId(senderId, opponentId)
                .map(ChatRoom::getChatId)
                .or(() -> {
                    if (createNewRoomIfNotExists) {
                        String chatId = createChatId(senderId, opponentId);
                        return Optional.of(chatId);
                    }
                    return Optional.empty();
                });
    }

    private String createChatId(String myId, String opponentId) {
        String chatRoomId = String.format("%s_%s", myId, opponentId);

        ChatRoom senderOpponent = ChatRoom.builder()
                .chatId(chatRoomId)
                .senderId(myId)
                .recipientId(opponentId)
                .build();

        ChatRoom opponentSender = ChatRoom.builder()
                .chatId(chatRoomId)
                .senderId(opponentId)
                .recipientId(myId)
                .build();

        chatRoomRepository.save(senderOpponent);
        chatRoomRepository.save(opponentSender);

        return chatRoomId;
    }


    public List<GetAllRoomsForUserResponse> getAllChatRooms(Integer userId) {
        List<GetAllRoomsForUserResponse> chatRoomsForUserResponse = new ArrayList<>();
        List<ChatRoom> allChatRoomsInDB = chatRoomRepository.findAll();

        String keyId = String.valueOf(userId);
        List<ChatRoom> chatRoomsForUser = allChatRoomsInDB.stream()
                .filter(chatRoom -> chatRoom.getChatId().contains(keyId)).toList();

        for (ChatRoom chatRoom : chatRoomsForUser) {
            String chatId = chatRoom.getChatId();

            if (chatRoom.getSenderId().equals(keyId)) {
                String opponentId = chatRoom.getRecipientId();

                UserEntity opponentUserEntity = userRepository.findById(Integer.valueOf(opponentId)).get();
                String opponentName = opponentUserEntity.getName();

                GetAllRoomsForUserResponse getAllRoomsForUserResponse = new GetAllRoomsForUserResponse(chatId, opponentName);
                chatRoomsForUserResponse.add(getAllRoomsForUserResponse);

            }
        }

        return chatRoomsForUserResponse;
    }
}
