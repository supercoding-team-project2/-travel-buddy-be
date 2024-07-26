package com.github.travelbuddy.common.controller;

import com.github.travelbuddy.routes.service.RouteDeleteService;
import com.github.travelbuddy.routes.dto.RouteDto;
import com.github.travelbuddy.routes.dto.RoutePutDto;
import com.github.travelbuddy.routes.service.RoutePostService;
import com.github.travelbuddy.routes.service.RouteGetService;
import com.github.travelbuddy.routes.service.RoutePutService;
import com.github.travelbuddy.users.dto.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/routes")
public class RouteController {

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
                RouteDto createdRoute = routePostService.createRouteWithDaysAndPlaces(routeDto, userId);
                return ResponseEntity.ok(createdRoute);
            } else if (routeDto != null && routeId != null && !routeDto.getDays().isEmpty()) {
                RouteDto updatedRoute = routePostService.addRouteDayAndPlaces(routeId, routeDto);
                return ResponseEntity.ok(updatedRoute);
            } else {
                throw new IllegalArgumentException("잘못된 요청입니다.");
            }
        } catch (Exception e) {
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
    public ResponseEntity<Map<String, String>> deleteRoute(@PathVariable Integer routeId) {
        try {
            CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Integer userId = userDetails.getUserId();

            String boardTitles = routeDeleteService.deleteRoute(routeId, userId);

            if (boardTitles == null) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "성공적으로 삭제되었습니다.");
                return ResponseEntity.ok(response);
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("error", "게시물이 있는 여행 경로입니다.");
                response.put("boardTitles", boardTitles);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "요청 처리 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }


    @DeleteMapping("/delete-boards/{routeId}")
    public ResponseEntity<String> deleteRouteWithBoards(@PathVariable Integer routeId) {
        try {
            routeDeleteService.deleteRouteWithBoards(routeId);
            return ResponseEntity.ok("성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
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
