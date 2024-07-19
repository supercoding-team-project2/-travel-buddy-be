package com.github.travelbuddy.routes.repository;

import com.github.travelbuddy.routes.entity.RouteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<RouteEntity, Integer> {
    List<RouteEntity> findByUserId(Integer userId);
}
