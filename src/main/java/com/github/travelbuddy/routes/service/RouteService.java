package com.github.travelbuddy.routes.service;

import com.github.travelbuddy.routes.dto.RouteDto;
import com.github.travelbuddy.routes.entity.RouteDayEntity;
import com.github.travelbuddy.routes.entity.RouteDayPlaceEntity;
import com.github.travelbuddy.routes.entity.RouteEntity;
import com.github.travelbuddy.routes.mapper.RouteMapper;
import com.github.travelbuddy.routes.repository.RouteDayRepository;
import com.github.travelbuddy.routes.repository.RouteDayPlaceRepository;
import com.github.travelbuddy.routes.repository.RouteRepository;
import com.github.travelbuddy.users.entity.UserEntity;
import com.github.travelbuddy.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RouteService {

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private RouteDayRepository routeDayRepository;

    @Autowired
    private RouteDayPlaceRepository routeDayPlaceRepository;

    @Autowired
    private RouteMapper routeMapper;

    @Autowired
    private UserRepository userRepository;

    public RouteDto createRouteWithDaysAndPlaces(RouteDto routeDto, Integer userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));

        RouteEntity route = routeMapper.toRouteEntity(routeDto);
        route.setUser(user);
        route.setCreatedAt(new Date());
        route = routeRepository.save(route);

        for (RouteDayEntity routeDay : route.getRouteDays()) {
            routeDay.setRoute(route);
            routeDayRepository.save(routeDay);
            for (RouteDayPlaceEntity routeDayPlace : routeDay.getRouteDayPlaces()) {
                routeDayPlace.setRouteDay(routeDay);
                routeDayPlaceRepository.save(routeDayPlace);
            }
        }

        return routeMapper.toRouteDto(route);
    }

    public RouteDto addRouteDayAndPlaces(Integer routeId, RouteDto routeDto) {
        RouteEntity route = routeRepository.findById(routeId)
                .orElseThrow(() -> new RuntimeException("경로를 찾을 수 없습니다"));

        for (RouteDto.RouteDayDto dayDto : routeDto.getDays()) {
            RouteDayEntity routeDay = routeMapper.toRouteDayEntity(dayDto, route);
            routeDay = routeDayRepository.save(routeDay);
            for (RouteDto.RouteDayPlaceDto placeDto : dayDto.getPlaces()) {
                RouteDayPlaceEntity routeDayPlace = routeMapper.toRouteDayPlaceEntity(placeDto, routeDay);
                routeDayPlaceRepository.save(routeDayPlace);
            }
        }

        return routeMapper.toRouteDto(route);
    }
}
