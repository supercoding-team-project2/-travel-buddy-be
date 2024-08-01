package com.github.travelbuddy.chat.entity;

import com.github.travelbuddy.chat.enums.Status;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document
public class ChatUser {
    @Id
    private String id;
    private Integer userId;
    private String userName;
    private Status status;
}
