package com.github.travelbuddy.routes.controller;

import com.github.travelbuddy.routes.dto.RouteDto;
import com.github.travelbuddy.routes.service.RouteService;
import com.github.travelbuddy.users.dto.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routes")
public class RouteController {

    @Autowired
    private RouteService routeService;

    @Transactional
    @PostMapping("/add")
    public RouteDto processRouteRequest(@RequestParam(required = false) Integer routeId,
                                        @RequestBody RouteDto routeDto) {

        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = userDetails.getUserId();

        if (routeDto != null && routeId == null) {
            return routeService.createRouteWithDaysAndPlaces(routeDto, userId);

        } else if (routeDto != null && routeId != null && !routeDto.getDays().isEmpty()) {
            return routeService.addRouteDayAndPlaces(routeId, routeDto);

        } else {
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }
    }
}
