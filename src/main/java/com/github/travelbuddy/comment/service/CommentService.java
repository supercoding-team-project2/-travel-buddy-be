package com.github.travelbuddy.comment.service;

import com.github.travelbuddy.board.entity.BoardEntity;
import com.github.travelbuddy.board.repository.BoardRepository;
import com.github.travelbuddy.comment.dto.CommentDTO;
import com.github.travelbuddy.comment.entity.CommentEntity;
import com.github.travelbuddy.comment.repository.CommentRepository;
import com.github.travelbuddy.comment.response.AddCommentResponse;
import com.github.travelbuddy.comment.response.AllCommentResponse;
import com.github.travelbuddy.comment.response.CommentResponse;
import com.github.travelbuddy.comment.response.ModifyCommentResponse;
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
            checkBoardExists(postId);

            BoardEntity boardEntity = boardRepository.findById(postId).get();

            List<CommentResponse> allCommentResponseList = new ArrayList<>();
            List<CommentEntity> comments = commentRepository.findAllByBoard(boardEntity);

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
                    .message("GET COMMENTS SUCCESS")
                    .commentList(allCommentResponseList)
                    .build();

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

    public ResponseEntity<?> addComment(Integer userId, CommentDTO commentDTO, Integer postId) {
        try {
            checkBoardExists(postId);

            UserEntity userEntity = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("아이디 " + userId + " 를 찾을 수 없습니다."));

            BoardEntity boardEntity = boardRepository.findById(postId).get();

            CommentEntity commentEntity = CommentEntity.builder()
                    .user(userEntity)
                    .board(boardEntity)
                    .content(commentDTO.getContent())
                    .build();

            commentRepository.save(commentEntity);

            AddCommentResponse addCommentResponse = AddCommentResponse.builder()
                    .postId(postId)
                    .message("ADD COMMENT COMPLETE")
                    .commentResponse(new CommentResponse(commentDTO.getContent(), userEntity.getName(), userEntity.getProfilePictureUrl()))
                    .build();

            return ResponseEntity.status(HttpStatus.OK).body(addCommentResponse);
        } catch (IllegalArgumentException ie) {
            log.info(ie.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ie.getMessage());
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    public ResponseEntity<?> modifyComment(Integer postId, Integer commentId, CommentDTO commentDTO) {
        try {
            checkBoardExists(postId);
            checkCommentExists(commentId);

            CommentEntity commentEntity = commentRepository.findById(commentId).get();

            commentEntity.setContent(commentDTO.getContent());
            commentRepository.save(commentEntity);

            CommentResponse commentResponse =
                    new CommentResponse(commentDTO.getContent(), commentEntity.getUser().getName(), commentEntity.getUser().getProfilePictureUrl());

            ModifyCommentResponse modifyCommentResponse = ModifyCommentResponse.builder()
                    .postId(postId)
                    .message("MODIFY COMMENT COMPLETED")
                    .commentResponse(commentResponse)
                    .build();

            return ResponseEntity.status(HttpStatus.OK).body(modifyCommentResponse);
        } catch (IllegalArgumentException ie) {
            log.info(ie.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ie.getMessage());
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    public ResponseEntity<?> deleteComment(Integer postId, Integer commentId) {
        try {
            checkBoardExists(postId);
            checkCommentExists(commentId);

            CommentEntity commentEntity = commentRepository.findById(commentId).get();

            commentRepository.delete(commentEntity);

            return ResponseEntity.status(HttpStatus.OK).body("DELETE COMMENT COMPLETED");
        } catch (IllegalArgumentException ie) {
            log.info(ie.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ie.getMessage());
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    public void checkBoardExists(Integer postId) {
        boolean isExistsBoard = boardRepository.existsById(postId);
        if (!isExistsBoard) throw new EntityNotFoundException("존재하지 않는 게시판입니다.");
    }

    private void checkCommentExists(Integer commentId) {
        boolean isExistsComment = commentRepository.existsById(commentId);
        if (!isExistsComment) throw new EntityNotFoundException("존재하지 않는 댓글입니다.");
    }
}
