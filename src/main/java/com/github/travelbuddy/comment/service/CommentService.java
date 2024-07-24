package com.github.travelbuddy.comment.service;

import com.github.travelbuddy.board.entity.BoardEntity;
import com.github.travelbuddy.board.repository.BoardRepository;
import com.github.travelbuddy.comment.dto.CommentDTO;
import com.github.travelbuddy.comment.entity.CommentEntity;
import com.github.travelbuddy.comment.repository.CommentRepository;
import com.github.travelbuddy.users.entity.UserEntity;
import com.github.travelbuddy.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    public void addComment(Integer userId, CommentDTO commentDTO, Integer postId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("아이디 " + userId + " 를 찾을 수 없습니다."));

        BoardEntity boardEntity = boardRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시물 " + postId + " 를 찾을 수 없습니다."));

        CommentEntity commentEntity = CommentEntity.builder()
                .user(userEntity)
                .post(boardEntity)
                .content(commentDTO.getContent())
                .build();

        commentRepository.save(commentEntity);
    }
}
