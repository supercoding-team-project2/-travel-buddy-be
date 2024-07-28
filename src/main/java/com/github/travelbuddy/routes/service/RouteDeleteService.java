package com.github.travelbuddy.routes.service;

import com.github.travelbuddy.board.entity.BoardEntity;
import com.github.travelbuddy.board.repository.BoardRepository;
import com.github.travelbuddy.comment.repository.CommentRepository;
import com.github.travelbuddy.likes.repository.LikesRepository;
import com.github.travelbuddy.postImage.repository.PostImageRepository;
import com.github.travelbuddy.routes.entity.RouteEntity;
import com.github.travelbuddy.routes.repository.RouteRepository;
import com.github.travelbuddy.trip.entity.TripEntity;
import com.github.travelbuddy.trip.repository.TripRepository;
import com.github.travelbuddy.usersInTravel.repository.UsersInTravelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RouteDeleteService {

    private final RouteRepository routeRepository;
    private final BoardRepository boardRepository;
    private final PostImageRepository postImageRepository;
    private final CommentRepository commentRepository;
    private final LikesRepository likesRepository;
    private final UsersInTravelRepository usersInTravelRepository;
    private final TripRepository tripRepository;

    public RouteDeleteService(RouteRepository routeRepository,
                              BoardRepository boardRepository,
                              PostImageRepository postImageRepository,
                              CommentRepository commentRepository,
                              LikesRepository likesRepository,
                              UsersInTravelRepository usersInTravelRepository,
                              TripRepository tripRepository) {
        this.routeRepository = routeRepository;
        this.boardRepository = boardRepository;
        this.postImageRepository = postImageRepository;
        this.commentRepository = commentRepository;
        this.likesRepository = likesRepository;
        this.usersInTravelRepository = usersInTravelRepository;
        this.tripRepository = tripRepository;
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
            TripEntity trip = tripRepository.findByBoard(board)
                    .orElseThrow(() -> new RuntimeException("여행 정보를 찾을 수 없습니다"));

            usersInTravelRepository.deleteAllByTrip(trip);
            tripRepository.delete(trip);
            postImageRepository.deleteAllByBoard(board);
            commentRepository.deleteAllByBoard(board);
            likesRepository.deleteAllByBoard(board);
            boardRepository.delete(board);
        }
        routeRepository.delete(route);
    }
}

