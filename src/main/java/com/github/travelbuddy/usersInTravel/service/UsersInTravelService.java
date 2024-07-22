package com.github.travelbuddy.usersInTravel.service;

import com.github.travelbuddy.trip.entity.TripEntity;
import com.github.travelbuddy.trip.repository.TripRepository;
import com.github.travelbuddy.users.dto.CustomUserDetails;
import com.github.travelbuddy.users.entity.UserEntity;
import com.github.travelbuddy.users.repository.UserRepository;
import com.github.travelbuddy.usersInTravel.entity.UsersInTravelEntity;
import com.github.travelbuddy.usersInTravel.repository.UsersInTravelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsersInTravelService {
    private final UsersInTravelRepository usersInTravelRepository;
    private final UserRepository userRepository;
    private final TripRepository tripRepository;

    @Transactional
    public void attendTrip(CustomUserDetails userDetails , Integer tripId) {
        Integer userId = userDetails.getUserId();
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("유저 찾을 수 없음"));
        TripEntity trip = tripRepository.findById(tripId).orElseThrow(() -> new IllegalArgumentException("여행 찾을 수 없음"));

        if (trip.getParticipantCount() >= trip.getTargetNumber()) {
            throw new IllegalArgumentException("참여 인원이 이미 가득 찼습니다.");
        }

        UsersInTravelEntity usersInTravel = UsersInTravelEntity.builder()
                .user(user)
                .trip(trip)
                .build();

        usersInTravelRepository.save(usersInTravel);

        trip.setParticipantCount(trip.getParticipantCount() + 1);
        tripRepository.save(trip);
    }

    @Transactional
    public void cancelTrip(CustomUserDetails userDetails, Integer tripId) {
        Integer userId = userDetails.getUserId();
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("유저 찾을 수 없음"));
        TripEntity trip = tripRepository.findById(tripId).orElseThrow(() -> new IllegalArgumentException("여행 찾을 수 없음"));

        UsersInTravelEntity usersInTravel = usersInTravelRepository.findByUserAndTrip(user, trip)
                .orElseThrow(() -> new IllegalArgumentException("여행 참여 정보를 찾을 수 없음"));

        usersInTravelRepository.delete(usersInTravel);

        trip.setParticipantCount(trip.getParticipantCount() - 1);
        tripRepository.save(trip);
    }
}
