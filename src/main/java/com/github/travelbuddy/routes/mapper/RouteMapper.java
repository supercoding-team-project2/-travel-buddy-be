package com.github.travelbuddy.routes.mapper;

import com.github.travelbuddy.routes.dto.RouteDto;
import com.github.travelbuddy.routes.dto.RoutePutDto;
import com.github.travelbuddy.routes.entity.RouteDayEntity;
import com.github.travelbuddy.routes.entity.RouteDayPlaceEntity;
import com.github.travelbuddy.routes.entity.RouteEntity;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class RouteMapper {

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

    public RouteEntity toRouteEntity(RouteDto routeDto) {
        RouteEntity route = RouteEntity.builder()
                .title(routeDto.getTitle())
                .description(routeDto.getDescription())
                .startAt(parseDate(routeDto.getStartAt()))
                .endAt(parseDate(routeDto.getEndAt()))
                .createdAt(LocalDateTime.now())
                .build();

        route.setRouteDays(routeDto.getDays().stream()
                .map(dayDto -> toRouteDayEntity(dayDto, route))
                .collect(Collectors.toList()));

        return route;
    }

    public RouteDto toRouteDto(RouteEntity routeEntity) {
        return RouteDto.builder()
                .routeId(routeEntity.getId())
                .title(routeEntity.getTitle())
                .description(routeEntity.getDescription())
                .startAt(formatDate(routeEntity.getStartAt()))
                .endAt(formatDate(routeEntity.getEndAt()))
                .createdAt(routeEntity.getCreatedAt())
                .days(routeEntity.getRouteDays().stream()
                        .map(this::toRouteDayDto)
                        .collect(Collectors.toList()))
                .build();
    }

    public RoutePutDto toRoutePutDto(RouteEntity routeEntity) {
        return RoutePutDto.builder()
                .title(routeEntity.getTitle())
                .description(routeEntity.getDescription())
                .build();
    }

    public RouteDayEntity toRouteDayEntity(RouteDto.RouteDayDto routeDayDto, RouteEntity routeEntity) {
        RouteDayEntity routeDay = RouteDayEntity.builder()
                .day(parseDate(routeDayDto.getDay()))
                .route(routeEntity)
                .build();

        routeDay.setRouteDayPlaces(routeDayDto.getPlaces().stream()
                .map(placeDto -> toRouteDayPlaceEntity(placeDto, routeDay))
                .collect(Collectors.toList()));

        return routeDay;
    }

    public RouteDto.RouteDayDto toRouteDayDto(RouteDayEntity routeDayEntity) {
        return RouteDto.RouteDayDto.builder()
                .day(formatDate(routeDayEntity.getDay()))
                .places(routeDayEntity.getRouteDayPlaces().stream()
                        .map(this::toRouteDayPlaceDto)
                        .collect(Collectors.toList()))
                .build();
    }

    public RouteDayPlaceEntity toRouteDayPlaceEntity(RouteDto.RouteDayPlaceDto routeDayPlaceDto, RouteDayEntity routeDayEntity) {
        return RouteDayPlaceEntity.builder()
                .placeName(routeDayPlaceDto.getPlaceName())
                .placeCategory(routeDayPlaceDto.getPlaceCategory())
                .routeDay(routeDayEntity)
                .build();
    }

    private RouteDto.RouteDayPlaceDto toRouteDayPlaceDto(RouteDayPlaceEntity routeDayPlaceEntity) {
        return RouteDto.RouteDayPlaceDto.builder()
                .placeName(routeDayPlaceEntity.getPlaceName())
                .placeCategory(routeDayPlaceEntity.getPlaceCategory())
                .build();
    }

    private String formatDate(Date date) {
        return date != null ? dateFormat.format(date) : null;
    }

    private Date parseDate(String dateString) {
        try {
            return dateString != null ? dateFormat.parse(dateString) : null;
        } catch (ParseException e) {
            throw new RuntimeException("날짜 포맷 오류", e);
        }
    }
}