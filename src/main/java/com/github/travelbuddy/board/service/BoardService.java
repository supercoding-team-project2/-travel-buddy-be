package com.github.travelbuddy.board.service;

import com.github.travelbuddy.board.dto.BoardAllDto;
import com.github.travelbuddy.board.entity.BoardEntity;
import com.github.travelbuddy.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public List<BoardAllDto> getAllBoards(String category, String sortBy, String order) {
        Sort sort = Sort.by(Sort.Direction.fromString(order), sortBy);
        List<Object[]> results = boardRepository.findAllWithRepresentativeImage(category, sort);
        return results.stream().map(result -> {
            BoardEntity board = (BoardEntity) result[0];
            String representativeImage = (String) result[1];
            return new BoardAllDto(board, representativeImage);
        }).collect(Collectors.toList());
    }
}
