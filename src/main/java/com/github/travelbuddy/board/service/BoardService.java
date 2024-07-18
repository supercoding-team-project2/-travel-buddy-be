package com.github.travelbuddy.board.service;

import com.github.travelbuddy.board.dto.BoardAllDto;
import com.github.travelbuddy.board.dto.BoardDetailDto;
import com.github.travelbuddy.board.entity.BoardEntity;
import com.github.travelbuddy.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {
    private final BoardRepository boardRepository;

    public List<BoardAllDto> getAllBoards(String category, Date startDate, Date endDate, String sortBy, String order) {
        log.info("Category: " + category);
        log.info("StartDate: " + startDate);
        log.info("EndDate: " + endDate);
        log.info("SortBy: " + sortBy);
        log.info("Order: " + order);

        if (sortBy == null) {
            sortBy = "createdAt";
        }
        if (order == null) {
            order = "desc";
        }
        List<Object[]> results = boardRepository.findAllWithRepresentativeImageAndDateRange(category, startDate, endDate, sortBy, order);
        return results.stream().map(result -> {
            Integer id = (Integer) result[0];
            BoardEntity.Category categoryEnum = BoardEntity.Category.valueOf((String) result[1]);
            String title = (String) result[2];
            String summary = (String) result[3];
            String author = (String) result[4];
            java.sql.Date startAt = (java.sql.Date) result[5];
            java.sql.Date endAt = (java.sql.Date) result[6];
            String representativeImage = (String) result[7];
            Long likeCount = (Long) result[8];
            return new BoardAllDto(id , categoryEnum, title, summary, author, startAt, endAt, representativeImage, likeCount);
        }).collect(Collectors.toList());
    }

    public BoardDetailDto getPostDetails(Integer postId) {
        List<Object[]> results = boardRepository.findPostDetailsById(postId);

        if (results == null || results.isEmpty()) {
            log.error("No query result found for postId: " + postId);
            throw new IllegalArgumentException("No query result found for postId: " + postId);
        }

        Object[] firstRow = results.get(0);

        BoardDetailDto.BoardDto boardDto = new BoardDetailDto.BoardDto(
            (Integer) firstRow[0],
            (String) firstRow[1],
            (String) firstRow[2],
            (String) firstRow[3],
            BoardEntity.Category.valueOf((String) firstRow[4]),
            (String) firstRow[5],
            ((Number) firstRow[8]).longValue(),
            results.stream().map(row -> (String) row[9]).distinct().collect(Collectors.toList())
        );

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, List<Map<String, String>>> sortedRouteDetails = new LinkedHashMap<>();

        for (Object[] row : results) {
            String routeDay = dateFormat.format((java.sql.Date) row[10]);
            String placeName = (String) row[11];
            String placeCategory = (String) row[12];

            Map<String, String> placeDetails = new LinkedHashMap<>();
            placeDetails.put("placeName", placeName);
            placeDetails.put("placeCategory", placeCategory);

            if (!sortedRouteDetails.containsKey(routeDay)) {
                sortedRouteDetails.put(routeDay, new ArrayList<>());
            }
            if (!sortedRouteDetails.get(routeDay).contains(placeDetails)) {
                sortedRouteDetails.get(routeDay).add(placeDetails);
            }
        }

        BoardDetailDto.RouteDto routeDto = new BoardDetailDto.RouteDto(
            (java.sql.Date) firstRow[6],
            (java.sql.Date) firstRow[7],
            sortedRouteDetails
        );

        BoardDetailDto.TripDto tripDto = new BoardDetailDto.TripDto(
            (Integer) firstRow[13],
            (Integer) firstRow[14],
            (Integer) firstRow[15],
            (Integer) firstRow[16],
            (String) firstRow[17]
        );

        return new BoardDetailDto(boardDto, routeDto, tripDto);
    }
}