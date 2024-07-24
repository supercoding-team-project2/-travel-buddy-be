package com.github.travelbuddy.common.controller;

import com.github.travelbuddy.comment.dto.CommentDTO;
import com.github.travelbuddy.comment.entity.CommentEntity;
import com.github.travelbuddy.comment.service.CommentService;
import com.github.travelbuddy.users.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment/")
@Slf4j
public class CommentController {

    private final CommentService commentService;
    List<CommentEntity> commentList = new ArrayList<>();

    @GetMapping("/{post_id}")
    public List<CommentEntity> getAllComments(@PathVariable int post_id) {
        return commentList;
    }

    @PostMapping("/add/{post_id}")
    public void addComment(@AuthenticationPrincipal CustomUserDetails userDetails,
                           @RequestBody CommentDTO commentDTO,
                           @PathVariable Integer post_id
    ) {
        log.info("=================== ADD COMMENT ===================");
        Integer userId = userDetails.getUserId();
        log.info("userId = " + userId);
        log.info("commentDTO = " + commentDTO);
        log.info("post_id = " + post_id);

        commentService.addComment(userId, commentDTO, post_id);
    }

    @PostMapping("/modify/{post_id}/{comment_id}")
    public void modifyComment(@PathVariable int post_id, @PathVariable int comment_id) {

    }

    @DeleteMapping("/delete/{post_id}/{comment_id}")
    public void deleteComment(@PathVariable int post_id, @PathVariable int comment_id) {

    }

}
