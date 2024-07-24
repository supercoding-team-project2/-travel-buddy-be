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

    @GetMapping("/{post_id}")
    public ApiResponse<?> getAllComments(@PathVariable Integer post_id) {
        List<CommentEntity> comments;
        CommentResponse commentResponse;

        try {
            comments = commentService.getAllComments(post_id);
            commentResponse = CommentResponse.builder().commentCount(comments.size()).comments(comments).build();
        }catch(Exception e) {
            log.info(e.getMessage());
            return ApiResponse.error(ErrorType.SYSTEM_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ApiResponse.success(commentResponse,"COMMENT ITEMS RESPONSE COMPLETE", HttpStatus.OK);
    }

    @PostMapping("/add/{post_id}")
    public ApiResponse<?> addComment(@AuthenticationPrincipal CustomUserDetails userDetails,
                                  @RequestBody CommentDTO commentDTO,
                                  @PathVariable Integer post_id
    ) {
        log.info("=================== ADD COMMENT ===================");
        Integer userId = userDetails.getUserId();
        log.info("userId = " + userId);
        log.info("commentDTO = " + commentDTO);
        log.info("post_id = " + post_id);

        return commentService.addComment(userId, commentDTO, post_id);
    }

    @PostMapping("/modify/{post_id}/{comment_id}")
    public ApiResponse<?> modifyComment(@PathVariable int post_id, @PathVariable int comment_id) {
        return commentService.modifyComment(post_id, comment_id);
    }

    @DeleteMapping("/delete/{post_id}/{comment_id}")
    public ApiResponse<?> deleteComment(@PathVariable int post_id, @PathVariable int comment_id) {
        return ApiResponse.success("DELETE COMPLETED", HttpStatus.OK);
    }

}
