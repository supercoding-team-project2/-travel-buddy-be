package com.github.travelbuddy.common.controller;

import com.github.travelbuddy.users.dto.CustomUserDetails;
import com.github.travelbuddy.usersInTravel.service.UsersInTravelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/attend")
@Slf4j
public class UsersInTravelController {
    private final UsersInTravelService usersInTravelService;

    @PostMapping("/{tripId}")
    public ResponseEntity<?> attendTrip(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Integer tripId) {
        try {
            usersInTravelService.attendTrip(userDetails, tripId);
            return ResponseEntity.status(HttpStatus.OK).body("여행에 성공적으로 참여했습니다.");
        } catch (IllegalArgumentException e) {
            log.error("여행 참여 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
