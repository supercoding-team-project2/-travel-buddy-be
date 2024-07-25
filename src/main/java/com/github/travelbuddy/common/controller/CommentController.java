package com.github.travelbuddy.common.controller;

import com.github.travelbuddy.comment.dto.CommentDTO;
import com.github.travelbuddy.comment.service.CommentService;
import com.github.travelbuddy.users.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        return commentService.addComment(userDetails.getUserId(), commentDTO, postId);
    }

    @PutMapping("/modify/{postId}/{commentId}")
    public ResponseEntity<?> modifyComment(@PathVariable Integer postId,
                                        @PathVariable Integer commentId,
                                        @RequestBody CommentDTO commentDTO) {
        log.info("=================== MODIFY COMMENT ===================");
        return commentService.modifyComment(postId, commentId, commentDTO);
    }

    @DeleteMapping("/delete/{postId}/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Integer postId, @PathVariable Integer commentId) {
        log.info("=================== DELETE COMMENT ===================");
        return commentService.deleteComment(postId, commentId);
    }
}
