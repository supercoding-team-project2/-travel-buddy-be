package com.github.travelbuddy.board.service;

import com.github.travelbuddy.board.dto.*;
import com.github.travelbuddy.board.entity.BoardEntity;
import com.github.travelbuddy.board.repository.BoardRepository;
import com.github.travelbuddy.common.service.S3Service;
import com.github.travelbuddy.postImage.entity.PostImageEntity;
import com.github.travelbuddy.postImage.repository.PostImageRepository;
import com.github.travelbuddy.routes.entity.RouteEntity;
import com.github.travelbuddy.routes.repository.RouteRepository;
import com.github.travelbuddy.trip.entity.TripEntity;
import com.github.travelbuddy.trip.repository.TripRepository;
import com.github.travelbuddy.trip.service.TripService;
import com.github.travelbuddy.users.dto.CustomUserDetails;
import com.github.travelbuddy.users.entity.UserEntity;
import com.github.travelbuddy.users.jwt.JWTUtill;
import com.github.travelbuddy.users.repository.UserRepository;
import com.github.travelbuddy.usersInTravel.repository.UsersInTravelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final RouteRepository routeRepository;
    private final PostImageRepository postImageRepository;
    private final TripRepository tripRepository;
    private final S3Service s3Service;
    private final TripService tripService;
    private final UsersInTravelRepository usersInTravelRepository;
    private final JWTUtill jwtUtill;

    public List<BoardAllDto> getAllBoards(String category, Date startDate, Date endDate, String sortBy, String order) {
        log.info("Category: " + category);
        log.info("StartDate: " + startDate);
        log.info("EndDate: " + endDate);
        log.info("SortBy: " + sortBy);
        log.info("Order: " + order);

        if (sortBy == null) {
            sortBy = "createdAt";
        }
        if (order == null) {
            order = "desc";
        }
        List<Object[]> results = boardRepository.findAllWithRepresentativeImageAndDateRange(category, startDate, endDate, sortBy, order);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return results.stream().map(result -> {
            Integer id = (Integer) result[0];
            BoardEntity.Category categoryEnum = BoardEntity.Category.valueOf((String) result[1]);
            String title = (String) result[2];
            String summary = (String) result[3];
            String author = (String) result[4];
            String startAt = dateFormat.format((Date) result[5]);
            String endAt = dateFormat.format((Date) result[6]);
            String representativeImage = (String) result[7];
            Long likeCount = (Long) result[8];
            return new BoardAllDto(id , categoryEnum, title, summary, author, startAt, endAt, representativeImage, likeCount);
        }).collect(Collectors.toList());
    }

    public BoardDetailDto getPostDetails(Integer postId) {
        List<Object[]> results = boardRepository.findPostDetailsById(postId);

        if (results == null || results.isEmpty()) {
            log.error("No query result found for postId: " + postId);
            throw new IllegalArgumentException("No query result found for postId: " + postId);
        }

        Object[] firstRow = results.get(0);

        BoardDetailDto.BoardDto boardDto = new BoardDetailDto.BoardDto(
            (Integer) firstRow[0],
            (String) firstRow[1],
            (String) firstRow[2],
            (String) firstRow[3],
            BoardEntity.Category.valueOf((String) firstRow[4]),
            (String) firstRow[5],
            ((Number) firstRow[8]).longValue(),
            results.stream().map(row -> (String) row[9]).distinct().collect(Collectors.toList())
        );

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, List<Map<String, String>>> sortedRouteDetails = new LinkedHashMap<>();

        for (Object[] row : results) {
            String routeDay = dateFormat.format((java.sql.Date) row[10]);
            String placeName = (String) row[11];
            String placeCategory = (String) row[12];

            Map<String, String> placeDetails = new LinkedHashMap<>();
            placeDetails.put("placeName", placeName);
            placeDetails.put("placeCategory", placeCategory);

            if (!sortedRouteDetails.containsKey(routeDay)) {
                sortedRouteDetails.put(routeDay, new ArrayList<>());
            }
            if (!sortedRouteDetails.get(routeDay).contains(placeDetails)) {
                sortedRouteDetails.get(routeDay).add(placeDetails);
            }
        }

        BoardDetailDto.RouteDto routeDto = new BoardDetailDto.RouteDto(
            (java.sql.Date) firstRow[6],
            (java.sql.Date) firstRow[7],
            sortedRouteDetails
        );

        BoardDetailDto.TripDto tripDto = new BoardDetailDto.TripDto(
            (Integer) firstRow[13],
            (Integer) firstRow[14],
            (Integer) firstRow[15],
            (Integer) firstRow[16],
            (String) firstRow[17]
        );

        return new BoardDetailDto(boardDto, routeDto, tripDto);
    }
    public BoardResponseDto<BoardSimpleDto> getBoardsByUserAndCategory(Integer userId, BoardEntity.Category category) {
        List<Object[]> results = boardRepository.findBoardsByUserIdAndCategory(userId, category);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<BoardSimpleDto> boardSimpleDtos =  results.stream().map(result -> new BoardSimpleDto(
                (Integer) result[0],
                (String) result[1],
                (String) result[2],
                (String) result[3],
                (BoardEntity.Category) result[4],
                ((LocalDateTime) result[5]).format(formatter)
        )).collect(Collectors.toList());

        String message;
        if (boardSimpleDtos.isEmpty()) {
            switch (category) {
                case REVIEW:
                    message = "아직 작성한 후기가 없습니다.";
                    break;
                case COMPANION:
                    message = "아직 작성한 동행 게시글이 없습니다.";
                    break;
                case GUIDE:
                    message = "아직 작성한 가이드 게시글이 없습니다.";
                    break;
                default:
                    message = "아직 작성한 게시물이 없습니다.";
            }
        } else {
            message = "게시물을 성공정으로 조회했습니다.";
        }
        return new BoardResponseDto<>(message , boardSimpleDtos);
    }

    public List<BoardAllDto> getLikedPostsByUser(Integer userId, BoardEntity.Category category, String sortBy) {
        if (sortBy == null) {
            sortBy = "createdAt";
        }
        List<Object[]> results = boardRepository.findLikedPostsByUserIdAndCategory(userId, category, sortBy);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return results.stream().map(result -> {
            Integer id = (Integer) result[0];
            BoardEntity.Category categoryEnum = (BoardEntity.Category) result[1];
            String title = (String) result[2];
            String summary = (String) result[3];
            String author = (String) result[4];
            String startAt = dateFormat.format((Date) result[5]);
            String endAt = dateFormat.format((Date) result[6]);
            Long likeCount = (Long) result[7];
            String representativeImage = (String) result[8];
            return new BoardAllDto(id, categoryEnum, title, summary, author, startAt, endAt, representativeImage, likeCount);
        }).collect(Collectors.toList());
    }

    @Transactional
    public void createBoard(BoardCreateDto createDto, CustomUserDetails userDetails) throws IOException {
        Integer userId = userDetails.getUserId();
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("유저 찾을 수 없음"));
        RouteEntity route = routeRepository.findById(createDto.getRouteId()).orElseThrow(() -> new IllegalArgumentException("경로 찾을 수 없음"));

        BoardEntity board = BoardEntity.builder()
                .user(user)
                .route(route)
                .title(createDto.getTitle())
                .summary(createDto.getSummary())
                .content(createDto.getContent())
                .category(createDto.getCategory())
                .createdAt(LocalDateTime.now())
                .build();

        boardRepository.save(board);

        if (createDto.getImages() != null) {
            for (MultipartFile image : createDto.getImages()) {
                String imageUrl = s3Service.uploadFile(image);
                PostImageEntity postImage = PostImageEntity.builder()
                        .board(board)
                        .url(imageUrl)
                        .build();
                postImageRepository.save(postImage);
            }
        }

        if (createDto.getCategory() == BoardEntity.Category.COMPANION || createDto.getCategory() == BoardEntity.Category.GUIDE){
            tripService.createTrip(user, board, createDto.getAgeMin(), createDto.getAgeMax(), createDto.getTargetNumber(), TripEntity.Gender.valueOf(createDto.getGender()));
        }
    }

    @Transactional
    public void updateBoard(BoardCreateDto updateDto, CustomUserDetails userDetails, Integer postId) throws IOException{
        Integer userId = userDetails.getUserId();
        BoardEntity board = boardRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        RouteEntity route = routeRepository.findById(updateDto.getRouteId()).orElseThrow(() -> new IllegalArgumentException("경로 찾을 수 없음"));

        if (!board.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("게시글을 수정할 권한이 없습니다.");
        }

        board.setRoute(route);
        board.setTitle(updateDto.getTitle());
        board.setSummary(updateDto.getSummary());
        board.setContent(updateDto.getContent());
        board.setCategory(updateDto.getCategory());
        board.setCreatedAt(LocalDateTime.now());

        boardRepository.save(board);

        if (updateDto.getImages() != null) {
            List<PostImageEntity> existingImages = postImageRepository.findAllByBoard(board);
            for (PostImageEntity image : existingImages) {
                s3Service.deleteFile(image.getUrl());
                postImageRepository.delete(image);
            }

            for (MultipartFile image : updateDto.getImages()) {
                String imageUrl = s3Service.uploadFile(image);
                PostImageEntity postImage = PostImageEntity.builder()
                        .board(board)
                        .url(imageUrl)
                        .build();
                postImageRepository.save(postImage);
            }
        }

        if (updateDto.getCategory() == BoardEntity.Category.COMPANION || updateDto.getCategory() == BoardEntity.Category.GUIDE) {
            tripService.updateTrip(userId , board, updateDto.getAgeMin(), updateDto.getAgeMax(), updateDto.getTargetNumber(), TripEntity.Gender.valueOf(updateDto.getGender()));
        }
    }

    @Transactional
    public void deleteBoard(Integer postId, CustomUserDetails userDetails) {
        Integer userId = userDetails.getUserId();
        BoardEntity board = boardRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        if (!board.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("게시글을 삭제할 권한이 없습니다.");
        }

        postImageRepository.deleteAllByBoard(board);

        tripRepository.deleteByBoard(board);

        boardRepository.delete(board);
    }

    public BoardResponseDto<BoardAllDto> getParticipatedTripsByUser(CustomUserDetails userDetails , BoardEntity.Category category, String sortBy, String order) {
        Integer userId = userDetails.getUserId();
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        if (sortBy == null) {
            sortBy = "createdAt";
        }
        if (order == null) {
            order = "desc";
        }

        if (category.equals(BoardEntity.Category.REVIEW)){
            String message = "리뷰 카테고리는 조회할 수 없습니다.";
            return new BoardResponseDto<>(message , null);
        }

        List<Object[]> results = usersInTravelRepository.findBoardsByUserWithLikeCountAndCategory(user, category, sortBy, order);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        List<BoardAllDto> participatedTrips = results.stream().map(result -> {
            BoardEntity board = (BoardEntity) result[0];
            Long likeCount = (Long) result[1];
            String startAt = dateFormat.format(board.getRoute().getStartAt());
            String endAt = dateFormat.format(board.getRoute().getEndAt());
            return new BoardAllDto(
                    board.getId(),
                    board.getCategory(),
                    board.getTitle(),
                    board.getSummary(),
                    board.getUser().getName(),
                    startAt,
                    endAt,
                    board.getPostImages().isEmpty() ? null : board.getPostImages().get(0).getUrl(),
                    likeCount
            );
        }).collect(Collectors.toList());

        String message = participatedTrips.isEmpty() ? "조회할 수 있는 데이터가 없습니다." : "참여한 여행 게시물을 성공적으로 조회했습니다.";
        return new BoardResponseDto<>(message, participatedTrips);
        }

        public BoardMainDto getTop6BoardsByCategories() {
            List<BoardMainSimpleDto> top6ReviewBoards = getTop6BoardsByCategory(BoardEntity.Category.REVIEW, "likeCount");
            List<BoardMainSimpleDto> top6GuideBoards = getTop6BoardsByCategory(BoardEntity.Category.GUIDE, "createdAt");
            List<BoardMainSimpleDto> top6CompanionBoards = getTop6BoardsByCategory(BoardEntity.Category.COMPANION, "createdAt");

            return new BoardMainDto(top6ReviewBoards, top6GuideBoards, top6CompanionBoards);
        }

        private List<BoardMainSimpleDto> getTop6BoardsByCategory(BoardEntity.Category category, String sortBy) {
            List<Object[]> results = boardRepository.findTop6BoardsByCategoryWithRepresentativeImage(category, sortBy);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            return results.stream().map(result -> {
                Integer id = (Integer) result[0];
                String title = (String) result[1];
                String createdAt = ((LocalDateTime) result[2]).format(formatter);
                Long likeCount = (Long) result[3];
                String representativeImage = (String) result[4];

                return new BoardMainSimpleDto(id, title, representativeImage, createdAt, likeCount);
            }).collect(Collectors.toList());
        }
    }