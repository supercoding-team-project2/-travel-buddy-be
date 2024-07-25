package com.github.travelbuddy.routes.service;

import com.github.travelbuddy.routes.entity.RouteEntity;
import com.github.travelbuddy.routes.repository.RouteRepository;
import com.github.travelbuddy.users.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RouteDeleteService {

    private final RouteRepository routeRepository;
    private final UserRepository userRepository;

    public RouteDeleteService(RouteRepository routeRepository, UserRepository userRepository) {
        this.routeRepository = routeRepository;
        this.userRepository = userRepository;
    }

    public void deleteRoute(Integer routeId, Integer userId) {
        RouteEntity route = routeRepository.findById(routeId)
                .orElseThrow(() -> new RuntimeException("경로를 찾을 수 없습니다"));

        if (!route.getUser().getId().equals(userId)) {
            throw new SecurityException("삭제 권한이 없습니다");
        }

        routeRepository.delete(route);
    }
}
