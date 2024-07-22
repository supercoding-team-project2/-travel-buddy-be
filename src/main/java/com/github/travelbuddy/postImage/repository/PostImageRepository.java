package com.github.travelbuddy.postImage.repository;

import com.github.travelbuddy.board.entity.BoardEntity;
import com.github.travelbuddy.postImage.entity.PostImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostImageRepository extends JpaRepository<PostImageEntity , Integer> {
    List<PostImageEntity> findAllByBoard(BoardEntity board);
}
