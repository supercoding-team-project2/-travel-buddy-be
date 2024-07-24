package com.github.travelbuddy.comment.service;

import com.github.travelbuddy.board.entity.BoardEntity;
import com.github.travelbuddy.board.repository.BoardRepository;
import com.github.travelbuddy.comment.dto.CommentDTO;
import com.github.travelbuddy.comment.entity.CommentEntity;
import com.github.travelbuddy.comment.repository.CommentRepository;
import com.github.travelbuddy.comment.response.CommentResponse;
import com.github.travelbuddy.common.enums.ErrorType;
import com.github.travelbuddy.common.response.ApiResponse;
import com.github.travelbuddy.users.entity.UserEntity;
import com.github.travelbuddy.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.xml.stream.events.Comment;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    public ApiResponse<?> getAllComments(Integer postId) {
        boolean isBoardEmpty = boardRepository.findById(postId).isEmpty();
        log.info("isBoardEmpty: " + isBoardEmpty);

        if (isBoardEmpty) {
            return ApiResponse.error(ErrorType.BOARD_IS_EMPTY, HttpStatus.BAD_REQUEST);
        }

        List<CommentResponse> commentResponseList = new ArrayList<>();

        try {

            List<CommentEntity> comments = commentRepository.findAllByPostId(postId);
            for (CommentEntity commentEntity : comments) {
                CommentResponse commentResponse = CommentResponse.builder()
                        .userName(commentEntity.getUser().getName())
                        .comment(commentEntity.getContent())
                        .profileImgUrl(commentEntity.getUser().getProfilePictureUrl())
                        .build();

                commentResponseList.add(commentResponse);
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            return ApiResponse.error(ErrorType.SYSTEM_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ApiResponse.success(commentResponseList, "COMMENT ITEMS RESPONSE COMPLETE", HttpStatus.OK);
    }


    public ApiResponse<?> addComment(Integer userId, CommentDTO commentDTO, Integer postId) {
        try {
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

            return ApiResponse.success("ADD COMMENT COMPLETED", HttpStatus.OK);
        } catch (Exception e) {
            log.info(e.getMessage());
            return ApiResponse.error(ErrorType.SYSTEM_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ApiResponse<?> modifyComment(int postId, int commentId) {
        return ApiResponse.success("MODIFY COMPLETED", HttpStatus.OK);
    }
}
