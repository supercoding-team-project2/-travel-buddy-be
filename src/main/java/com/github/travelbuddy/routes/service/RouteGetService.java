package com.github.travelbuddy.routes.service;

import com.github.travelbuddy.routes.dto.RouteDto;
import com.github.travelbuddy.routes.entity.RouteEntity;
import com.github.travelbuddy.routes.mapper.RouteMapper;
import com.github.travelbuddy.routes.repository.RouteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RouteGetService {

    private final RouteRepository routeRepository;
    private final RouteMapper routeMapper;

    public RouteGetService(RouteRepository routeRepository, RouteMapper routeMapper) {
        this.routeRepository = routeRepository;
        this.routeMapper = routeMapper;
    }

    public List<RouteDto> getRoutesByUserId(Integer userId) {
        List<RouteEntity> routeEntities = routeRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return routeEntities.stream()
                .map(routeMapper::toRouteDto)
                .collect(Collectors.toList());
    }
}
