package com.github.travelbuddy.board.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardMainDto {
    private List<BoardMainSimpleDto> top4ReviewBoards;
    private List<BoardMainSimpleDto> top4GuideBoards;
    private List<BoardMainSimpleDto> top4CompanionBoards;
}
