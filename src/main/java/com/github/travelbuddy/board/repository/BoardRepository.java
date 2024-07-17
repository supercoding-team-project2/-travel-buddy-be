package com.github.travelbuddy.board.repository;

import com.github.travelbuddy.board.entity.BoardEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Integer> {
    @Query(value = "SELECT b.id ,b.category, b.title, b.summary, b.suggestion, u.name AS author, r.start_at, r.end_at, pi.url AS representative_image " +
                   "FROM boards b " +
                   "LEFT JOIN users u ON b.user_id = u.id " +
                   "LEFT JOIN routes r ON b.route_id = r.id " +
                   "LEFT JOIN post_imgs pi ON b.id = pi.post_id " +
                   "WHERE (:category IS NULL OR b.category = :category) " +
                   "AND (r.start_at BETWEEN :startDate AND :endDate OR r.end_at BETWEEN :startDate AND :endDate) " +
                   "AND pi.id = (SELECT pi2.id FROM post_imgs pi2 WHERE pi2.post_id = b.id ORDER BY pi2.id LIMIT 1) " +
                   "ORDER BY " +
                   "CASE WHEN :sortBy = 'createdAt' AND :order = 'desc' THEN b.created_at END DESC, " +
                   "CASE WHEN :sortBy = 'createdAt' AND :order = 'asc' THEN b.created_at END ASC, " +
                   "CASE WHEN :sortBy = 'suggestion' AND :order = 'desc' THEN b.suggestion END DESC, " +
                   "CASE WHEN :sortBy = 'suggestion' AND :order = 'asc' THEN b.suggestion END ASC, " +
                   "CASE WHEN :sortBy = 'title' AND :order = 'asc' THEN b.title END ASC, " +
                   "CASE WHEN :sortBy = 'title' AND :order = 'desc' THEN b.title END DESC", nativeQuery = true)
    List<Object[]> findAllWithRepresentativeImageAndDateRange(@Param("category") String category, @Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("sortBy") String sortBy, @Param("order") String order);
}