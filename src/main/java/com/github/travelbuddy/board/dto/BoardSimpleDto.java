package com.github.travelbuddy.board.dto;

import com.github.travelbuddy.board.entity.BoardEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardSimpleDto {
    private Integer id;
    private String title;
    private String summary;
    private String representativeImage;
    private BoardEntity.Category category;
    private String createdAt;
}
