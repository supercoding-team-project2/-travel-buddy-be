package com.github.travelbuddy.common.controller;

import com.github.travelbuddy.comment.dto.CommentDTO;
import com.github.travelbuddy.comment.entity.CommentEntity;
import com.github.travelbuddy.comment.response.CommentResponse;
import com.github.travelbuddy.comment.service.CommentService;
import com.github.travelbuddy.common.enums.ErrorType;
import com.github.travelbuddy.common.response.ApiResponse;
import com.github.travelbuddy.users.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment/")
@Slf4j
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{postId}")
    public ApiResponse<?> getAllComments(@PathVariable Integer postId) {
        log.info("=================== GET ALL COMMENT ===================");
        log.info("postId = " + postId);
        return commentService.getAllComments(postId);
    }

    @PostMapping("/add/{postId}")
    public ApiResponse<?> addComment(@AuthenticationPrincipal CustomUserDetails userDetails,
                                     @RequestBody CommentDTO commentDTO,
                                     @PathVariable Integer postId
    ) {
        log.info("=================== ADD COMMENT ===================");
        Integer userId = userDetails.getUserId();
        log.info("userId = " + userId);
        log.info("commentDTO = " + commentDTO);
        log.info("postId = " + postId);

        return commentService.addComment(userId, commentDTO, postId);
    }

    @PostMapping("/modify/{postId}/{comment_id}")
    public ApiResponse<?> modifyComment(@PathVariable int postId, @PathVariable int comment_id) {
        return commentService.modifyComment(postId, comment_id);
    }

    @DeleteMapping("/delete/{postId}/{comment_id}")
    public ApiResponse<?> deleteComment(@PathVariable int postId, @PathVariable int comment_id) {
        return ApiResponse.success("DELETE COMPLETED", HttpStatus.OK);
    }

}
