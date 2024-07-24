package com.github.travelbuddy.common.controller;

import com.github.travelbuddy.likes.service.LikesService;
import com.github.travelbuddy.users.jwt.JWTUtill;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
@Slf4j
public class LikesController {
    private final LikesService likesService;
    private final JWTUtill jwtUtill;

    @PostMapping("/{postId}")
    public void likeUp(@PathVariable Integer postId, HttpServletRequest request) {
        log.info(request.getMethod());
        String token = request.getHeader("Authorization");
        log.info("token: " + token);
        Integer userId = jwtUtill.getUserId(token);
        log.info("userId: " + userId);

        likesService.processLike(postId, userId, request.getMethod());
    }

    @DeleteMapping("/{postId}")
    public void likeDown(@PathVariable Integer postId, HttpServletRequest request) {
        log.info(request.getMethod());
        String token = request.getHeader("Authorization");
        Integer userId = jwtUtill.getUserId(token);
        log.info("userId: " + userId);

        likesService.processLike(postId, userId, request.getMethod());
    }

}
