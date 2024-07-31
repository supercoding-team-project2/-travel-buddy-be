package com.github.travelbuddy.common.controller;

import com.github.travelbuddy.users.dto.CustomUserDetails;
import com.github.travelbuddy.usersInTravel.dto.ParticipantTripResponse;
import com.github.travelbuddy.usersInTravel.service.UsersInTravelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
        } catch (ResponseStatusException e) {
            log.error("여행 참여 중 오류 발생", e);
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

    @DeleteMapping("/{tripId}")
    public ResponseEntity<?> cancelTrip(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Integer tripId) {
        try {
            usersInTravelService.cancelTrip(userDetails, tripId);
            return ResponseEntity.status(HttpStatus.OK).body("여행 참여가 성공적으로 취소되었습니다.");
        } catch (ResponseStatusException e) {
            log.error("여행 참여 취소 중 오류 발생", e);
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

    @GetMapping("/{tripId}")
    public ResponseEntity<?> participantTrip(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Integer tripId) {
        try {
            Boolean result = usersInTravelService.participantTrip(userDetails, tripId);
            ParticipantTripResponse response = new ParticipantTripResponse("여행 참여자 조회 결과 입니다.", result);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }catch (ResponseStatusException e){
            log.error("여행 참여자 조회 중 오류 발생" , e);
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }
}
