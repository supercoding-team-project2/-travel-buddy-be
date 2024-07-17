package com.github.travelbuddy.board.repository;

import com.github.travelbuddy.board.entity.BoardEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity , Integer> {
    @Query("SELECT b, pi.url AS representativeImage FROM BoardEntity b LEFT JOIN PostImageEntity pi ON b.id = pi.board.id WHERE (:category IS NULL OR b.category = :category) GROUP BY b.id")
    List<Object[]> findAllWithRepresentativeImage(@Param("category") String category, Sort sort);
}
