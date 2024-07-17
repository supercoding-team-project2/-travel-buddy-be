package com.github.travelbuddy.common.controller;

import com.github.travelbuddy.board.dto.BoardAllDto;
import com.github.travelbuddy.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardController {
    private final BoardService boardService;

    @GetMapping
    public List<BoardAllDto> getAllBoards(
            @RequestParam(required = false) String category,
            @RequestParam(required = false, defaultValue = "createdAt") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String order) {
        return boardService.getAllBoards(category, sortBy, order);
    }
}
