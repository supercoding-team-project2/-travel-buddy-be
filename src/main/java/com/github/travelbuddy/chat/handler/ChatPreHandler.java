package com.github.travelbuddy.chat.handler;

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

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String authorization = "";

        if (accessor.getCommand().equals(StompCommand.CONNECT)) {
            authorization = String.valueOf(accessor.getNativeHeader("Authorization"))
                                            .replace("[","")
                                            .replace("]","");

            log.info("authorization in chatPreHandler = " + authorization);
        }

        return message;
    }
}
