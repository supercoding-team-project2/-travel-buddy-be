package com.github.travelbuddy.common.controller;

import com.github.travelbuddy.likes.service.LikesService;
import com.github.travelbuddy.users.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
@Slf4j
public class LikesController {
    private final LikesService likesService;

    @GetMapping("/info/{postId}")
    public ResponseEntity<?> likeInfo(@PathVariable Integer postId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return likesService.getLikeInfo(postId, userDetails.getUserId());
    }

    @PostMapping("/{postId}")
    public ResponseEntity<?> likeUp(@PathVariable Integer postId,
                                    @AuthenticationPrincipal CustomUserDetails userDetails) {
        return likesService.processLikeUp(postId, userDetails.getUserId());
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> likeDown(@PathVariable Integer postId,
                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        return likesService.processLikeDown(postId, userDetails.getUserId());
    }
}
