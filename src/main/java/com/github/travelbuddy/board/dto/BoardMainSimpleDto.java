package com.github.travelbuddy.board.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardMainSimpleDto {
    private Integer id;
    private String title;
    private String representativeImage;
    private String createdAt;
    private Long likeCount;
}
