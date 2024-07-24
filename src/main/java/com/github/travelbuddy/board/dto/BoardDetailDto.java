package com.github.travelbuddy.board.dto;

import com.github.travelbuddy.board.entity.BoardEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardDetailDto {
    private BoardDto board;
    private RouteDto route;
    private TripDto trip;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BoardDto {
        private Integer id;
        private String title;
        private String summary;
        private String content;
        private BoardEntity.Category category;
        private String author;
        private String userProfile;
        private Long likeCount;
        private List<String> images;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RouteDto {
        private Integer id;
        private Date startAt;
        private Date endAt;
        private Map<String, List<Map<String, String>>> routeDetails;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TripDto {
        private Integer id;
        private Integer ageMin;
        private Integer ageMax;
        private Integer targetNumber;
        private Integer participantCount;
        private String gender;
    }
}