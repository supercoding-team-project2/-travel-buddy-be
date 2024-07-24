package com.github.travelbuddy.chat.repository;

import com.github.travelbuddy.chat.entity.ChatUser;
import com.github.travelbuddy.chat.enums.Status;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatUserRepository extends MongoRepository<ChatUser, String> {
    ChatUser findByUserName(String userName);
    List<ChatUser> findAllByStatus(Status status);
    boolean existsByUserName(String userName);
}
