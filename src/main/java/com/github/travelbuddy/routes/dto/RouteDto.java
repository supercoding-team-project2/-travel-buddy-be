package com.github.travelbuddy.routes.dto;

import com.github.travelbuddy.routes.enums.PlaceCategory;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RouteDto {

    private Integer routeId;
    private String title;
    private String description;
    private String startAt;
    private String endAt;
    private LocalDateTime createdAt;

    private List<RouteDayDto> days;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RouteDayDto {
        private String day;
        private List<RouteDayPlaceDto> places;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RouteDayPlaceDto {
        private String placeName;
        private PlaceCategory placeCategory;
    }
}