package com.github.travelbuddy.board.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardResponseDto<T> {
    private String message;
    private List<T> data;
}
