package com.github.travelbuddy.usersInTravel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantTripResponse {
    private String message;
    private Boolean result;
}
