package com.github.travelbuddy.common.controller;

import com.github.travelbuddy.likes.service.LikesService;
import com.github.travelbuddy.users.dto.CustomUserDetails;
import com.github.travelbuddy.users.jwt.JWTUtill;
import jakarta.servlet.http.HttpServletRequest;
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
    private final JWTUtill jwtUtill;

    @PostMapping("/{postId}")
    public ResponseEntity<?> likeUp(@PathVariable Integer postId,
                                    @AuthenticationPrincipal CustomUserDetails userDetails,
                                    HttpServletRequest request) {
        log.info(request.getMethod());
        return likesService.processLike(postId, userDetails.getUserId(), request.getMethod());
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> likeDown(@PathVariable Integer postId,
                         @AuthenticationPrincipal CustomUserDetails userDetails,
                         HttpServletRequest request) {
        log.info(request.getMethod());
        return likesService.processLike(postId, userDetails.getUserId(), request.getMethod());
    }
}
