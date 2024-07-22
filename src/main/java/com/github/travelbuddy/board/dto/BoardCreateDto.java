package com.github.travelbuddy.board.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.travelbuddy.board.entity.BoardEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BoardCreateDto {
    private Integer routeId;
    private String title;
    private String summary;
    private String content;
    private BoardEntity.Category category;
    private List<MultipartFile> images;
    private LocalDateTime createdAt;
    private Integer ageMin;
    private Integer ageMax;
    private Integer targetNumber;
    private String gender;
}
