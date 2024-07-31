package com.github.travelbuddy.chat.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomEnterDto {
    private Integer senderId;
    private Integer opponentId;
}
