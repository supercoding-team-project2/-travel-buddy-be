package com.github.travelbuddy.likes.service;

import com.github.travelbuddy.board.entity.BoardEntity;
import com.github.travelbuddy.board.repository.BoardRepository;
import com.github.travelbuddy.likes.entity.LikesEntity;
import com.github.travelbuddy.likes.repository.LikesRepository;
import com.github.travelbuddy.likes.response.LikeInfoResponse;
import com.github.travelbuddy.likes.response.LikeResponse;
import com.github.travelbuddy.users.entity.UserEntity;
import com.github.travelbuddy.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class LikesService {
    private final LikesRepository likesRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    public ResponseEntity<?> processLikeUp(Integer postId, Integer userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("아이디 " + userId + " 를 찾을 수 없습니다."));

        BoardEntity boardEntity = boardRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시물 " + postId + " 를 찾을 수 없습니다."));

        boolean isExistsLikeEntity = likesRepository.existsByUserIdAndBoardId(userId, postId);

        if (isExistsLikeEntity) return ResponseEntity.status(HttpStatus.CONFLICT).body("ALREADY LIKE");

        LikesEntity likesEntity = LikesEntity.builder()
                .user(userEntity)
                .board(boardEntity)
                .build();

        likesRepository.save(likesEntity);

        LikeResponse likeResponse = new LikeResponse();
        likeResponse.setUserId(userId);
        likeResponse.setBoardId(postId);
        likeResponse.setMessage("LIKES UP");

        return ResponseEntity.status(HttpStatus.CREATED).body(likeResponse);

    }

    public ResponseEntity<?> processLikeDown(Integer postId, Integer userId) {
        LikesEntity likesEntity = likesRepository.findByUserIdAndBoardId(userId, postId);
        likesRepository.delete(likesEntity);

        LikeResponse likeResponse = new LikeResponse();
        likeResponse.setUserId(userId);
        likeResponse.setBoardId(postId);
        likeResponse.setMessage("LIKES DELETED");
        log.info("likeResponse = " + likeResponse);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(likeResponse);
    }

    public boolean likeStatus(Integer userId, Integer postId) {
        LikesEntity likesEntity = likesRepository.findByUserIdAndBoardId(userId, postId);
        if (likesEntity == null) {
            return false;
        } else {
            return true;
        }
    }

    public ResponseEntity<?> getLikeInfo(Integer postId, Integer userId) {
        boolean isLike = likeStatus(userId, postId);

        BoardEntity boardEntity = boardRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시물 " + postId + " 를 찾을 수 없습니다."));

        Integer likeCount = likesRepository.countAllByBoard(boardEntity);

        LikeInfoResponse likeInfoResponse = LikeInfoResponse.builder()
                .count(likeCount)
                .isLike(isLike)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(likeInfoResponse);
    }
}
