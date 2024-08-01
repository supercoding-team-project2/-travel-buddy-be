package com.github.travelbuddy.chat.repository;

import com.github.travelbuddy.chat.entity.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.math.BigInteger;
import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, BigInteger> {
    List<ChatMessage> findAllBySenderIdAndChatId(String senderId, String chatId);
}
