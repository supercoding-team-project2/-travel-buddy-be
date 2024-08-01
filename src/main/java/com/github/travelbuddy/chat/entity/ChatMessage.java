package com.github.travelbuddy.chat.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Document
public class ChatMessage {
    @Id // id를 String, ObjectId, BigInteger 중 하나로 사용해야 함
    private String id;
    private String chatId;
    private String senderId;
    private String opponentId;
    private String content;
    private String timeStamp;
}
