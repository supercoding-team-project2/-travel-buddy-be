package com.github.travelbuddy.board.dto;

import com.github.travelbuddy.board.entity.BoardEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardAllDto {
    private Integer id;
    private BoardEntity.Category categoryEnum;
    private String title;
    private String summary;
    private String author;
    private Date startAt;
    private Date endAt;
    private String representativeImage;
    private Integer likeCount;
}
