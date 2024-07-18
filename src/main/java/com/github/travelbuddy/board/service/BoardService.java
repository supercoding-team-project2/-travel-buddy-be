package com.github.travelbuddy.board.service;

import com.github.travelbuddy.board.dto.BoardAllDto;
import com.github.travelbuddy.board.entity.BoardEntity;
import com.github.travelbuddy.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
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
}