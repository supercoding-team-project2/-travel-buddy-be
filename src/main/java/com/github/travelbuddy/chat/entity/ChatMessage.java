package com.github.travelbuddy.chat.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;
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
    private BigInteger id;
    private String chatId;
    private String senderName;
    private String recipientName;
    private String content;
    private Date timestamp;
}
