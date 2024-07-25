package com.github.travelbuddy.comment.service;

import com.github.travelbuddy.board.entity.BoardEntity;
import com.github.travelbuddy.board.repository.BoardRepository;
import com.github.travelbuddy.comment.dto.CommentDTO;
import com.github.travelbuddy.comment.entity.CommentEntity;
import com.github.travelbuddy.comment.repository.CommentRepository;
import com.github.travelbuddy.comment.response.AllCommentResponse;
import com.github.travelbuddy.comment.response.CommentResponse;
import com.github.travelbuddy.users.entity.UserEntity;
import com.github.travelbuddy.users.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    public ResponseEntity<?> getAllComments(Integer postId) {
        try {
            boolean isNotExistsBoard = boardRepository.findById(postId).isEmpty();
            if (isNotExistsBoard) throw new EntityNotFoundException("존재하지 않는 게시판입니다.");

            List<CommentResponse> allCommentResponseList = new ArrayList<>();
            List<CommentEntity> comments = commentRepository.findAllByPostId(postId);

            for (CommentEntity commentEntity : comments) {
                CommentResponse commentResponse = CommentResponse.builder()
                        .userName(commentEntity.getUser().getName())
                        .comment(commentEntity.getContent())
                        .profileImgUrl(commentEntity.getUser().getProfilePictureUrl())
                        .build();

                allCommentResponseList.add(commentResponse);
            }

            AllCommentResponse allCommentResponse = AllCommentResponse.builder()
                    .postId(postId)
                    .message("SUCCESS")
                    .commentList(allCommentResponseList)
                    .build();

            System.out.println("allCommentResponse = " + allCommentResponse);

            if (allCommentResponse.getCommentList().isEmpty())
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(allCommentResponse);

            return ResponseEntity.status(HttpStatus.OK).body(allCommentResponse);

        } catch (IllegalArgumentException ie) {
            log.info(ie.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ie.getMessage());
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    public CommentResponse addComment(Integer userId, CommentDTO commentDTO, Integer postId) {
        boolean isBoardEmpty = boardRepository.findById(postId).isEmpty();
        log.info("isBoardEmpty: " + isBoardEmpty);

        if (isBoardEmpty) {
            throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
        }

        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("아이디 " + userId + " 를 찾을 수 없습니다."));

        BoardEntity boardEntity = boardRepository.findById(postId).get();

        CommentEntity commentEntity = CommentEntity.builder()
                .user(userEntity)
                .post(boardEntity)
                .content(commentDTO.getContent())
                .build();

        commentRepository.save(commentEntity);

        return new CommentResponse(commentDTO.getContent(), userEntity.getName(), userEntity.getProfilePictureUrl());
    }
}
