package com.github.travelbuddy.common.controller;

import com.github.travelbuddy.comment.dto.CommentDTO;
import com.github.travelbuddy.comment.response.CommentResponse;
import com.github.travelbuddy.comment.service.CommentService;
import com.github.travelbuddy.users.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment/")
@Slf4j
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{postId}")
    public ResponseEntity<?> getAllComments(@PathVariable Integer postId) {
        log.info("=================== GET ALL COMMENT ===================");
        return commentService.getAllComments(postId);
    }

    @PostMapping("/add/{postId}")
    public ResponseEntity<?> addComment(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                     @RequestBody CommentDTO commentDTO,
                                                     @PathVariable Integer postId
    ) {
        log.info("=================== ADD COMMENT ===================");
        Integer userId = userDetails.getUserId();

        CommentResponse commentResponse;

        try {
            commentResponse = commentService.addComment(userId, commentDTO, postId);
        } catch (IllegalArgumentException nfe) {
            log.info(nfe.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(nfe.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(commentResponse);
    }
}
