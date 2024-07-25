package com.github.travelbuddy.routes.mapper;

import com.github.travelbuddy.routes.dto.RouteDto;
import com.github.travelbuddy.routes.dto.RoutePutDto;
import com.github.travelbuddy.routes.entity.RouteDayEntity;
import com.github.travelbuddy.routes.entity.RouteDayPlaceEntity;
import com.github.travelbuddy.routes.entity.RouteEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Component
public class RouteMapper {

    public RouteEntity toRouteEntity(RouteDto routeDto) {
        RouteEntity route = new RouteEntity();
        route.setTitle(routeDto.getTitle());
        route.setDescription(routeDto.getDescription());
        route.setStartAt(routeDto.getStartAt());
        route.setEndAt(routeDto.getEndAt());
        route.setCreatedAt(LocalDateTime.now());
        route.setRouteDays(routeDto.getDays().stream()
                .map(dayDto -> toRouteDayEntity(dayDto, route))
                .collect(Collectors.toList()));
        return route;
    }

    public RouteDto toRouteDto(RouteEntity routeEntity) {
        RouteDto routeDto = new RouteDto();
        routeDto.setTitle(routeEntity.getTitle());
        routeDto.setDescription(routeEntity.getDescription());
        routeDto.setStartAt(routeEntity.getStartAt());
        routeDto.setEndAt(routeEntity.getEndAt());
        routeDto.setCreatedAt(java.sql.Timestamp.valueOf(routeEntity.getCreatedAt()));
        routeDto.setDays(routeEntity.getRouteDays().stream()
                .map(this::toRouteDayDto)
                .collect(Collectors.toList()));
        return routeDto;
    }

    public RoutePutDto toRoutePutDto(RouteEntity routeEntity) {
        RoutePutDto routePutDto = new RoutePutDto();
        routePutDto.setTitle(routeEntity.getTitle());
        routePutDto.setDescription(routeEntity.getDescription());
        return routePutDto;
    }

    public RouteDayEntity toRouteDayEntity(RouteDto.RouteDayDto routeDayDto, RouteEntity routeEntity) {
        RouteDayEntity routeDay = new RouteDayEntity();
        routeDay.setDay(routeDayDto.getDay());
        routeDay.setRoute(routeEntity);
        routeDay.setRouteDayPlaces(routeDayDto.getPlaces().stream()
                .map(placeDto -> toRouteDayPlaceEntity(placeDto, routeDay))
                .collect(Collectors.toList()));
        return routeDay;
    }

    private RouteDto.RouteDayDto toRouteDayDto(RouteDayEntity routeDayEntity) {
        RouteDto.RouteDayDto routeDayDto = new RouteDto.RouteDayDto();
        routeDayDto.setDay(routeDayEntity.getDay());
        routeDayDto.setPlaces(routeDayEntity.getRouteDayPlaces().stream()
                .map(this::toRouteDayPlaceDto)
                .collect(Collectors.toList()));
        return routeDayDto;
    }

    public RouteDayPlaceEntity toRouteDayPlaceEntity(RouteDto.RouteDayPlaceDto routeDayPlaceDto, RouteDayEntity routeDayEntity) {
        RouteDayPlaceEntity routeDayPlace = new RouteDayPlaceEntity();
        routeDayPlace.setPlaceName(routeDayPlaceDto.getPlaceName());
        routeDayPlace.setPlaceCategory(routeDayPlaceDto.getPlaceCategory());
        routeDayPlace.setRouteDay(routeDayEntity);
        return routeDayPlace;
    }

    private RouteDto.RouteDayPlaceDto toRouteDayPlaceDto(RouteDayPlaceEntity routeDayPlaceEntity) {
        RouteDto.RouteDayPlaceDto routeDayPlaceDto = new RouteDto.RouteDayPlaceDto();
        routeDayPlaceDto.setPlaceName(routeDayPlaceEntity.getPlaceName());
        routeDayPlaceDto.setPlaceCategory(routeDayPlaceEntity.getPlaceCategory());
        return routeDayPlaceDto;
    }
}