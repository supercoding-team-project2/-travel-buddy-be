package com.github.travelbuddy.likes.repository;

import com.github.travelbuddy.board.entity.BoardEntity;
import com.github.travelbuddy.likes.entity.LikesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<LikesEntity, Integer> {
    LikesEntity findByUserIdAndBoardId(Integer userId, Integer boardId);
    boolean existsByUserIdAndBoardId(Integer userId, Integer boardId);
    Integer countAllByBoard(BoardEntity board);

    void deleteAllByBoard(BoardEntity board);
}
