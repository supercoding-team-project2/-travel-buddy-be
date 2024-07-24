package com.github.travelbuddy.chat.service;

import com.github.travelbuddy.chat.entity.ChatUser;
import com.github.travelbuddy.chat.enums.Status;
import com.github.travelbuddy.chat.repository.ChatUserRepository;
import com.github.travelbuddy.users.entity.UserEntity;
import com.github.travelbuddy.users.jwt.JWTUtill;
import com.github.travelbuddy.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatUserService {
    private final JWTUtill jwtUtill;
    private final UserRepository userRepository;
    private final ChatUserRepository chatUserRepository;
    List<ChatUser> chatUserList = new ArrayList<>();

    public void addUser(String token) {
        log.info("=================== ADD CHATUSER ===================");

        Integer userId = jwtUtill.getUserId(token);
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("아이디 " + userId + " 를 찾을 수 없습니다."));
        String userName = userEntity.getName();

        boolean isExistChatUser = chatUserRepository.existsByUserName(userName);

        if(isExistChatUser) {
            ChatUser chatUser = chatUserRepository.findByUserName(userName);
            chatUser.setStatus(Status.ONLINE);
        }else {
            ChatUser chatUser = new ChatUser();
            chatUser.setUserName(userName);
            chatUser.setStatus(Status.ONLINE);

            chatUserRepository.save(chatUser);
        }
    }

    public void disconnect(String token) {
        log.info("token: " + token);

        Integer userId = jwtUtill.getUserId(token);
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("아이디 " + userId + " 를 찾을 수 없습니다."));
        String userName = userEntity.getName();

        ChatUser chatUser = chatUserRepository.findByUserName(userName);
        chatUser.setStatus(Status.OFFLINE);
    }

    public List<ChatUser> findConnectedUsers() {
        return chatUserRepository.findAllByStatus(Status.ONLINE);
    }
}
