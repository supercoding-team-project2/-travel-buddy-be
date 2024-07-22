package com.github.travelbuddy.trip.service;

import com.github.travelbuddy.board.entity.BoardEntity;
import com.github.travelbuddy.trip.entity.TripEntity;
import com.github.travelbuddy.trip.repository.TripRepository;
import com.github.travelbuddy.users.entity.UserEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TripService {
    private final TripRepository tripRepository;

    @Transactional
    public void createTrip(UserEntity user, BoardEntity board, Integer ageMin, Integer ageMax, Integer targetNumber, TripEntity.Gender gender) {
        TripEntity trip = TripEntity.builder()
                .user(user)
                .board(board)
                .ageMin(ageMin)
                .ageMax(ageMax)
                .targetNumber(targetNumber)
                .participantCount(0)
                .gender(gender)
                .build();

        tripRepository.save(trip);
    }

    @Transactional
    public void updateTrip(Integer userId, BoardEntity board, Integer ageMin, Integer ageMax, Integer targetNumber, TripEntity.Gender gender) {
        TripEntity trip = tripRepository.findByBoard(board).orElseThrow(() -> new IllegalArgumentException("여행 정보를 찾을 수 없습니다."));

        if (!trip.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("여행 정보를 수정할 권한이 없습니다.");
        }

        trip.setAgeMin(ageMin);
        trip.setAgeMax(ageMax);
        trip.setTargetNumber(targetNumber);
        trip.setGender(gender);

        tripRepository.save(trip);
    }
}
