package com.github.travelbuddy.routes.service;

import com.github.travelbuddy.board.entity.BoardEntity;
import com.github.travelbuddy.board.repository.BoardRepository;
import com.github.travelbuddy.postImage.entity.PostImageEntity;
import com.github.travelbuddy.postImage.repository.PostImageRepository;
import com.github.travelbuddy.routes.entity.RouteEntity;
import com.github.travelbuddy.routes.repository.RouteRepository;
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

    public RouteDeleteService(RouteRepository routeRepository, BoardRepository boardRepository, PostImageRepository postImageRepository) {
        this.routeRepository = routeRepository;
        this.boardRepository = boardRepository;
        this.postImageRepository = postImageRepository;
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
            List<PostImageEntity> postImages = board.getPostImages();
            for (PostImageEntity postImage : postImages) {
                postImageRepository.delete(postImage);
            }

            boardRepository.delete(board);
        }

        routeRepository.delete(route);
    }
}
