package com.github.travelbuddy.comment.repository;

import com.github.travelbuddy.board.entity.BoardEntity;
import com.github.travelbuddy.comment.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {
    List<CommentEntity> findAllByPostId(Integer post_id);

    void deleteAllByBoard(BoardEntity board);
}
