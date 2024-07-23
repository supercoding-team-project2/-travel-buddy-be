package com.github.travelbuddy.trip.repository;

import com.github.travelbuddy.board.entity.BoardEntity;
import com.github.travelbuddy.trip.entity.TripEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TripRepository extends JpaRepository<TripEntity , Integer> {
    Optional<TripEntity> findByBoard(BoardEntity board);

    void deleteByBoard(BoardEntity board);
}
