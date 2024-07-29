package com.github.travelbuddy.usersInTravel.service;

import com.github.travelbuddy.trip.entity.TripEntity;
import com.github.travelbuddy.trip.repository.TripRepository;
import com.github.travelbuddy.users.dto.CustomUserDetails;
import com.github.travelbuddy.users.entity.UserEntity;
import com.github.travelbuddy.users.repository.UserRepository;
import com.github.travelbuddy.usersInTravel.entity.UsersInTravelEntity;
import com.github.travelbuddy.usersInTravel.repository.UsersInTravelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsersInTravelService {
    private final UsersInTravelRepository usersInTravelRepository;
    private final UserRepository userRepository;
    private final TripRepository tripRepository;

    @Transactional
    public void attendTrip(CustomUserDetails userDetails , Integer tripId) {
        Integer userId = userDetails.getUserId();
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"유저 찾을 수 없음"));
        TripEntity trip = tripRepository.findById(tripId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"여행 찾을 수 없음"));

        int koreanAge = calculateAge(user.getResidentNum());
        log.info(String.valueOf(koreanAge));
        if (koreanAge < trip.getAgeMin() || koreanAge > trip.getAgeMax()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "참여 가능한 나이가 아닙니다.");
        }

        if(userId.equals(trip.getUser().getId())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "작성자는 참여신청을 할 수 없습니다.");
        }

        if (trip.getParticipantCount() >= trip.getTargetNumber()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"참여 인원이 이미 가득 찼습니다.");
        }

        UsersInTravelEntity usersInTravel = UsersInTravelEntity.builder()
                .user(user)
                .trip(trip)
                .build();

        usersInTravelRepository.save(usersInTravel);

        trip.setParticipantCount(trip.getParticipantCount() + 1);
        tripRepository.save(trip);
    }

    private int calculateAge(String residentNum){
        String birthDateString = residentNum.substring(0,6);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        LocalDate birthDate = LocalDate.parse(birthDateString,formatter);

        int centuryIndicator = Character.getNumericValue(residentNum.charAt(6));
        int century = (centuryIndicator == 1 || centuryIndicator == 2) ? 1900 : 2000;
        birthDate = birthDate.withYear(century + birthDate.getYear() % 100);

        LocalDate currentDate = LocalDate.now();
        int age = currentDate.getYear() - birthDate.getYear() + 1;
        return age;
    }

    @Transactional
    public void cancelTrip(CustomUserDetails userDetails, Integer tripId) {
        Integer userId = userDetails.getUserId();
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"유저 찾을 수 없음"));
        TripEntity trip = tripRepository.findById(tripId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"여행 찾을 수 없음"));

        UsersInTravelEntity usersInTravel = usersInTravelRepository.findByUserAndTrip(user, trip)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"여행 참여 정보를 찾을 수 없음"));

        usersInTravelRepository.delete(usersInTravel);

        trip.setParticipantCount(trip.getParticipantCount() - 1);
        tripRepository.save(trip);
    }
}
