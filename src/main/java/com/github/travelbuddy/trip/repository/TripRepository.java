package com.github.travelbuddy.trip.repository;

import com.github.travelbuddy.trip.entity.TripEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripRepository extends JpaRepository<TripEntity , Integer> {
}
