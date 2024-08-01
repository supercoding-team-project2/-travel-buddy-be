package com.github.travelbuddy.chat.response;

import com.github.travelbuddy.chat.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetAllRoomsForUserResponse {
    private String roomId;
    private String opponentName;
    private Status status;
}
