package com.github.travelbuddy.chat.service;

import com.github.travelbuddy.chat.entity.ChatMessage;
import com.github.travelbuddy.chat.entity.ChatRoom;
import com.github.travelbuddy.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository repository;
    private final ChatRoomService chatRoomService;
    private final ChatMessageRepository chatMessageRepository;

    public ChatMessage save(ChatMessage chatMessage) {
        log.info("chatMessage.getTimestamp() = " + chatMessage.getTimeStamp());
        String chatId = chatRoomService.getChatRoomId(chatMessage.getSenderId(), chatMessage.getOpponentId(), true)
                .orElseThrow();
        log.info("chatId = " + chatId);

        chatMessage.setChatId(chatId);
        repository.save(chatMessage);

        return chatMessage;
    }

    public List<ChatMessage> findChatMessages(String chatId) {
        return chatMessageRepository.findAllByChatId(chatId);
    }
}
