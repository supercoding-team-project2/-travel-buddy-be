package com.github.travelbuddy.comment.repository;

import com.github.travelbuddy.comment.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {
}
