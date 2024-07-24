package com.github.travelbuddy.chat.repository;


import com.github.travelbuddy.chat.entity.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.math.BigInteger;
import java.util.Optional;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, BigInteger> {
    Optional<ChatRoom> findBySenderIdAndRecipientId(String senderId, String recipientId);
}
