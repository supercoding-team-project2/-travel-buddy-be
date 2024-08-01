package com.github.travelbuddy.chat.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetAllRoomsForUserResponse {
    private String roomId;
    private String opponentName;
}
