package com.github.travelbuddy.chat.handler;

import com.github.travelbuddy.users.jwt.JWTUtill;
import com.github.travelbuddy.users.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatPreHandler implements ChannelInterceptor {

    private final JWTUtill jwtUtill;
    private final UserService userService;
    Long memberId = 0L;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String authorization = "";

        if (accessor.getCommand().equals(StompCommand.CONNECT)) {
            authorization = String.valueOf(accessor.getNativeHeader("Authorization"))
                                            .replace("[","")
                                            .replace("]","");

            log.info("authorization = " + authorization);

            boolean isExpiredToken = jwtUtill.isExpired(authorization);
            log.info("isExpiredToken = " + isExpiredToken);
        }

        return message;
    }
}
