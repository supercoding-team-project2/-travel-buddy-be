package com.github.travelbuddy.routes.repository;

import com.github.travelbuddy.routes.entity.RouteDayPlaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteDayPlaceRepository extends JpaRepository<RouteDayPlaceEntity, Integer> {
}
