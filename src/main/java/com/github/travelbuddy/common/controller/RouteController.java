package com.github.travelbuddy.common.controller;

import com.github.travelbuddy.routes.dto.RouteDto;
import com.github.travelbuddy.routes.dto.RoutePutDto;
import com.github.travelbuddy.routes.service.RoutePostService;
import com.github.travelbuddy.routes.service.RouteDeleteService;
import com.github.travelbuddy.routes.service.RouteGetService;
import com.github.travelbuddy.routes.service.RoutePutService;
import com.github.travelbuddy.users.dto.CustomUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routes")
public class RouteController {

    private static final Logger log = LoggerFactory.getLogger(RouteController.class);


    private final RoutePostService routePostService;
    private final RouteDeleteService routeDeleteService;
    private final RouteGetService routeGetService;
    private final RoutePutService routePutService;

    public RouteController(RoutePostService routePostService, RouteDeleteService routeDeleteService, RouteGetService routeGetService, RoutePutService routePutService) {
        this.routePostService = routePostService;
        this.routeDeleteService = routeDeleteService;
        this.routeGetService = routeGetService;
        this.routePutService = routePutService;
    }

    @PostMapping("/add")
    public ResponseEntity<RouteDto> postRoute(@RequestParam(required = false) Integer routeId,
                                              @RequestBody RouteDto routeDto) {
        try {
            CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Integer userId = userDetails.getUserId();

            if (routeDto != null && routeId == null) {
                log.info("새로운 경로 생성");
                RouteDto createdRoute = routePostService.createRouteWithDaysAndPlaces(routeDto, userId);
                log.info("생성된 경로 DTO: {}", createdRoute);
                return ResponseEntity.ok(createdRoute);
            } else if (routeDto != null && routeId != null && !routeDto.getDays().isEmpty()) {
                log.info("기존 경로에 새로운 날과 장소 추가");
                RouteDto updatedRoute = routePostService.addRouteDayAndPlaces(routeId, routeDto);
                log.info("업데이트된 경로 DTO: {}", updatedRoute);
                return ResponseEntity.ok(updatedRoute);
            } else {
                log.warn("잘못된 요청입니다. routeDto: {}, routeId: {}", routeDto, routeId);
                throw new IllegalArgumentException("잘못된 요청입니다.");
            }
        } catch (Exception e) {
            log.error("오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/update/{routeId}")
    public ResponseEntity<RoutePutDto> putRoute(@PathVariable Integer routeId,
                                                @RequestBody RoutePutDto routePutDto) {
        try {
            CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Integer userId = userDetails.getUserId();

            RoutePutDto updatedRoute = routePutService.putRoute(routeId, routePutDto, userId);
            return ResponseEntity.ok(updatedRoute);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/delete/{routeId}")
    public ResponseEntity<String> deleteRoute(@PathVariable Integer routeId) {

        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = userDetails.getUserId();

        routeDeleteService.deleteRoute(routeId, userId);
        return ResponseEntity.ok("성공적으로 삭제되었습니다.");
    }

    @GetMapping("/list")
    public ResponseEntity<List<RouteDto>> getRoutes() {
        try {
            CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Integer userId = userDetails.getUserId();

            List<RouteDto> routes = routeGetService.getRoutesByUserId(userId);
            return ResponseEntity.ok(routes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}