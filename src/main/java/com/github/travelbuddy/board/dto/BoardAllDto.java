package com.github.travelbuddy.board.dto;

import com.github.travelbuddy.board.entity.BoardEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;

@Data
@AllArgsConstructor
public class BoardAllDto {
    private Integer id;
    private String categoryEnum;
    private String title;
    private String summary;
    private Integer suggestion;
    private String author;
    private Date startAt;
    private Date endAt;
    private String representativeImage;
}
