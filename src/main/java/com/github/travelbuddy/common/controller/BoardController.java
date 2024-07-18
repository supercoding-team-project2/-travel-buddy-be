package com.github.travelbuddy.common.controller;

import com.github.travelbuddy.board.dto.BoardAllDto;
import com.github.travelbuddy.board.dto.BoardDetailDto;
import com.github.travelbuddy.board.dto.BoardSimpleDto;
import com.github.travelbuddy.board.entity.BoardEntity;
import com.github.travelbuddy.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardController {
    private final BoardService boardService;

    @GetMapping
    public List<BoardAllDto> getAllBoards(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
            @RequestParam(required = false, defaultValue = "createdAt") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String order) {
        return boardService.getAllBoards(category, startDate, endDate, sortBy, order);
    }

    @GetMapping("/{postId}")
    public BoardDetailDto getPostDetails(@PathVariable Integer postId) {
        return boardService.getPostDetails(postId);
    }

    @GetMapping("/my")
    public List<BoardSimpleDto> getBoardsByUserAndCategory(
            @RequestParam Integer userId,
            @RequestParam BoardEntity.Category category) {
        return boardService.getBoardsByUserAndCategory(userId, category);
    }
}