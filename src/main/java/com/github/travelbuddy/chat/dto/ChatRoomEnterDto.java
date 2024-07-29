package com.github.travelbuddy.chat.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomEnterDto {
    private Integer myId;
    private Integer opponentId;
}
