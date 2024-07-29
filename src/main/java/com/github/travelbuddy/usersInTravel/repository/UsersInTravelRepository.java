package com.github.travelbuddy.usersInTravel.repository;

import com.github.travelbuddy.board.entity.BoardEntity;
import com.github.travelbuddy.trip.entity.TripEntity;
import com.github.travelbuddy.users.entity.UserEntity;
import com.github.travelbuddy.usersInTravel.entity.UsersInTravelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsersInTravelRepository extends JpaRepository<UsersInTravelEntity , Integer> {
    @Query("SELECT b, COUNT(l.id) as likeCount " +
            "FROM UsersInTravelEntity u " +
            "JOIN u.trip t " +
            "JOIN t.board b " +
            "JOIN b.route r " +
            "LEFT JOIN LikesEntity l ON b.id = l.board.id " +
            "WHERE u.user = :user " +
            "AND (:category IS NULL OR b.category = :category) " +
            "AND (:startDate IS NULL OR :endDate IS NULL OR (DATE(r.startAt) <= DATE(:endDate) AND DATE(r.endAt) >= DATE(:startDate))) " +
            "GROUP BY b " +
            "ORDER BY " +
            "CASE WHEN :sortBy = 'createdAt' AND :order = 'desc' THEN b.createdAt END DESC, " +
            "CASE WHEN :sortBy = 'createdAt' AND :order = 'asc' THEN b.createdAt END ASC, " +
            "CASE WHEN :sortBy = 'likes' AND :order = 'desc' THEN COUNT(l.id) END DESC, " +
            "CASE WHEN :sortBy = 'likes' AND :order = 'asc' THEN COUNT(l.id) END ASC, " +
            "CASE WHEN :sortBy = 'title' AND :order = 'desc' THEN b.title END DESC, " +
            "CASE WHEN :sortBy = 'title' AND :order = 'asc' THEN b.title END ASC")
    List<Object[]> findBoardsByUserWithLikeCountAndCategory(
            @Param("user") UserEntity user,
            @Param("category") BoardEntity.Category category,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("sortBy") String sortBy,
            @Param("order") String order);

    Optional<UsersInTravelEntity> findByUserAndTrip(UserEntity user, TripEntity trip);

    void deleteAllByTrip(TripEntity trip);
}
