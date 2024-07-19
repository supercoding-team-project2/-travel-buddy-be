package com.github.travelbuddy.routes.dto;

import com.github.travelbuddy.routes.enums.PlaceCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RouteDto {

    private String title;
    private String description;
    private Date startAt;
    private Date endAt;

    private List<RouteDayDto> days;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RouteDayDto {
        private Date day;
        private List<RouteDayPlaceDto> places;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RouteDayPlaceDto {
        private String placeName;
        private PlaceCategory placeCategory;
    }
}
