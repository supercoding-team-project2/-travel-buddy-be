package com.github.travelbuddy.common.controller;

import com.github.travelbuddy.board.dto.BoardAllDto;
import com.github.travelbuddy.board.dto.BoardDetailDto;
import com.github.travelbuddy.board.dto.BoardResponseDto;
import com.github.travelbuddy.board.dto.BoardSimpleDto;
import com.github.travelbuddy.board.entity.BoardEntity;
import com.github.travelbuddy.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
@Slf4j
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
    public BoardResponseDto<BoardSimpleDto> getBoardsByUserAndCategory(
            @RequestParam Integer userId,
            @RequestParam(required = false , defaultValue = "REVIEW") BoardEntity.Category category) {
        return boardService.getBoardsByUserAndCategory(userId, category);
    }

    @GetMapping("/liked")
    public ResponseEntity<BoardResponseDto<BoardAllDto>> getLikedPostsByUser(
            @RequestParam Integer userId,
            @RequestParam(required = false) BoardEntity.Category category,
            @RequestParam(required = false) String sortBy) {
        List<BoardAllDto> likedPosts = boardService.getLikedPostsByUser(userId, category, sortBy);
        String message = likedPosts.isEmpty() ? "추천한 게시물이 없습니다." : "추천한 게시물을 성공적으로 조회했습니다.";
        return ResponseEntity.ok(new BoardResponseDto<>(message, likedPosts));
    }
}