package com.github.travelbuddy.routes.repository;

import com.github.travelbuddy.routes.entity.RouteDayEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteDayRepository extends JpaRepository<RouteDayEntity, Integer> {
}
