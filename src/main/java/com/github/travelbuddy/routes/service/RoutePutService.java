package com.github.travelbuddy.routes.service;

import com.github.travelbuddy.routes.dto.RoutePutDto;
import com.github.travelbuddy.routes.entity.RouteEntity;
import com.github.travelbuddy.routes.mapper.RouteMapper;
import com.github.travelbuddy.routes.repository.RouteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoutePutService {

    private final RouteRepository routeRepository;
    private final RouteMapper routeMapper;

    public RoutePutService(RouteRepository routeRepository, RouteMapper routeMapper) {
        this.routeRepository = routeRepository;
        this.routeMapper = routeMapper;
    }

    public RoutePutDto putRoute(Integer routeId, RoutePutDto routePutDto, Integer userId) {
        RouteEntity route = routeRepository.findById(routeId)
                .orElseThrow(() -> new RuntimeException("경로를 찾을 수 없습니다"));

        if (!route.getUser().getId().equals(userId)) {
            throw new SecurityException("수정 권한이 없습니다");
        }

        route.setTitle(routePutDto.getTitle());
        route.setDescription(routePutDto.getDescription());
        routeRepository.save(route);

        return routeMapper.toRoutePutDto(route);
    }
}