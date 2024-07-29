package com.github.travelbuddy.common.controller;

import com.github.travelbuddy.routes.dto.RouteDto;
import com.github.travelbuddy.routes.dto.RoutePutDto;
import com.github.travelbuddy.routes.service.RouteService;
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

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @PostMapping("/add")
    public ResponseEntity<RouteDto> postRoute(@RequestParam(required = false) Integer routeId,
                                              @RequestBody RouteDto routeDto) {
        try {
            CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Integer userId = userDetails.getUserId();

            if (routeDto != null && routeId == null) {
                RouteDto createdRoute = routeService.createRouteWithDaysAndPlaces(routeDto, userId);
                return ResponseEntity.ok(createdRoute);
            } else if (routeDto != null && routeId != null && !routeDto.getDays().isEmpty()) {
                RouteDto updatedRoute = routeService.addRouteDayAndPlaces(routeId, routeDto);
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

            RoutePutDto updatedRoute = routeService.putRoute(routeId, routePutDto, userId);
            return ResponseEntity.ok(updatedRoute);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/delete/{routeId}")
    public ResponseEntity<Map<String, String>> deleteRoute(@PathVariable Integer routeId) {
        Map<String, String> response = new HashMap<>();
        try {
            CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Integer userId = userDetails.getUserId();

            String boardTitles = routeService.deleteRoute(routeId, userId);

            if (boardTitles == null) {
                response.put("message", "성공적으로 삭제되었습니다.");
                return ResponseEntity.ok(response);
            } else {
                response.put("message", "게시물이 있는 여행 경로입니다.");
                response.put("boardTitles", boardTitles);
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
            }
        } catch (SecurityException se) {
            response.put("error", "삭제 권한이 없습니다.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        } catch (Exception e) {
            response.put("error", "요청 처리 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/delete-boards/{routeId}")
    public ResponseEntity<String> deleteRouteWithBoards(@PathVariable Integer routeId) {
        try {
            routeService.deleteRouteWithBoards(routeId);
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

            List<RouteDto> routes = routeService.getRoutesByUserId(userId);
            return ResponseEntity.ok(routes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
