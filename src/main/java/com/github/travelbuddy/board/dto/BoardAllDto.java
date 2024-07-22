package com.github.travelbuddy.board.dto;

import com.github.travelbuddy.board.entity.BoardEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardAllDto {
    private Integer id;
    private BoardEntity.Category categoryEnum;
    private String title;
    private String summary;
    private String author;
    private String startAt;
    private String endAt;
    private String representativeImage;
    private Long likeCount;
}
