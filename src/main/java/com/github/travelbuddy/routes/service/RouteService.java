package com.github.travelbuddy.routes.service;

import com.github.travelbuddy.routes.dto.RouteDto;
import com.github.travelbuddy.routes.dto.RoutePutDto;
import com.github.travelbuddy.routes.entity.RouteDayEntity;
import com.github.travelbuddy.routes.entity.RouteDayPlaceEntity;
import com.github.travelbuddy.routes.entity.RouteEntity;
import com.github.travelbuddy.routes.mapper.RouteMapper;
import com.github.travelbuddy.routes.repository.RouteDayRepository;
import com.github.travelbuddy.routes.repository.RouteDayPlaceRepository;
import com.github.travelbuddy.routes.repository.RouteRepository;
import com.github.travelbuddy.users.entity.UserEntity;
import com.github.travelbuddy.users.repository.UserRepository;
import com.github.travelbuddy.board.entity.BoardEntity;
import com.github.travelbuddy.board.repository.BoardRepository;
import com.github.travelbuddy.comment.repository.CommentRepository;
import com.github.travelbuddy.likes.repository.LikesRepository;
import com.github.travelbuddy.postImage.repository.PostImageRepository;
import com.github.travelbuddy.trip.entity.TripEntity;
import com.github.travelbuddy.trip.repository.TripRepository;
import com.github.travelbuddy.usersInTravel.repository.UsersInTravelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class RouteService {

    private final RouteRepository routeRepository;
    private final RouteDayRepository routeDayRepository;
    private final RouteDayPlaceRepository routeDayPlaceRepository;
    private final RouteMapper routeMapper;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final PostImageRepository postImageRepository;
    private final CommentRepository commentRepository;
    private final LikesRepository likesRepository;
    private final UsersInTravelRepository usersInTravelRepository;
    private final TripRepository tripRepository;

    public RouteService(RouteRepository routeRepository,
                        RouteDayRepository routeDayRepository,
                        RouteDayPlaceRepository routeDayPlaceRepository,
                        RouteMapper routeMapper,
                        UserRepository userRepository,
                        BoardRepository boardRepository,
                        PostImageRepository postImageRepository,
                        CommentRepository commentRepository,
                        LikesRepository likesRepository,
                        UsersInTravelRepository usersInTravelRepository,
                        TripRepository tripRepository) {
        this.routeRepository = routeRepository;
        this.routeDayRepository = routeDayRepository;
        this.routeDayPlaceRepository = routeDayPlaceRepository;
        this.routeMapper = routeMapper;
        this.userRepository = userRepository;
        this.boardRepository = boardRepository;
        this.postImageRepository = postImageRepository;
        this.commentRepository = commentRepository;
        this.likesRepository = likesRepository;
        this.usersInTravelRepository = usersInTravelRepository;
        this.tripRepository = tripRepository;
    }

    public RouteDto createRouteWithDaysAndPlaces(RouteDto routeDto, Integer userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));

        RouteEntity route = routeMapper.toRouteEntity(routeDto);
        route.setUser(user);
        route = routeRepository.save(route);

        for (RouteDayEntity routeDay : route.getRouteDays()) {
            routeDay.setRoute(route);
            routeDay = routeDayRepository.save(routeDay);
            for (RouteDayPlaceEntity routeDayPlace : routeDay.getRouteDayPlaces()) {
                routeDayPlace.setRouteDay(routeDay);
                routeDayPlaceRepository.save(routeDayPlace);
            }
        }

        return routeMapper.toRouteDto(route);
    }

    public RouteDto addRouteDayAndPlaces(Integer routeId, RouteDto routeDto) {
        RouteEntity route = routeRepository.findById(routeId)
                .orElseThrow(() -> new RuntimeException("경로를 찾을 수 없습니다"));

        for (RouteDto.RouteDayDto dayDto : routeDto.getDays()) {
            RouteDayEntity routeDay = routeMapper.toRouteDayEntity(dayDto, route);
            routeDay = routeDayRepository.save(routeDay);
            for (RouteDto.RouteDayPlaceDto placeDto : dayDto.getPlaces()) {
                RouteDayPlaceEntity routeDayPlace = routeMapper.toRouteDayPlaceEntity(placeDto, routeDay);
                routeDayPlaceRepository.save(routeDayPlace);
            }
        }

        return routeMapper.toRouteDto(route);
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

    public String deleteRoute(Integer routeId, Integer userId) {
        RouteEntity route = routeRepository.findById(routeId)
                .orElseThrow(() -> new RuntimeException("경로를 찾을 수 없습니다"));

        if (!route.getUser().getId().equals(userId)) {
            throw new SecurityException("삭제 권한이 없습니다");
        }

        List<BoardEntity> boards = route.getBoards();

        if (boards.isEmpty()) {
            routeRepository.delete(route);
            return null;
        } else {
            String boardTitles = boards.stream()
                    .map(BoardEntity::getTitle)
                    .collect(Collectors.joining(", "));
            return boardTitles;
        }
    }

    public void deleteRouteWithBoards(Integer routeId) {
        RouteEntity route = routeRepository.findById(routeId)
                .orElseThrow(() -> new RuntimeException("경로를 찾을 수 없습니다"));

        List<BoardEntity> boards = route.getBoards();

        for (BoardEntity board : boards) {
            Optional<TripEntity> optionalTrip = tripRepository.findByBoard(board);
            if (optionalTrip.isPresent()) {
                TripEntity trip = optionalTrip.get();

                usersInTravelRepository.deleteAllByTrip(trip);
                tripRepository.delete(trip);
            }
            postImageRepository.deleteAllByBoard(board);
            commentRepository.deleteAllByBoard(board);
            likesRepository.deleteAllByBoard(board);
            boardRepository.delete(board);
        }
        routeRepository.delete(route);
    }

    public List<RouteDto> getRoutesByUserId(Integer userId) {
        List<RouteEntity> routeEntities = routeRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return routeEntities.stream()
                .map(routeMapper::toRouteDto)
                .collect(Collectors.toList());
    }
}
