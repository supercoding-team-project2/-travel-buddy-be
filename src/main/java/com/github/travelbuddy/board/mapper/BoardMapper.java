package com.github.travelbuddy.board.mapper;

import com.github.travelbuddy.board.dto.BoardAllDto;
import com.github.travelbuddy.board.entity.BoardEntity;
import com.github.travelbuddy.postImage.entity.PostImageEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface BoardMapper {

    BoardMapper INSTANCE = Mappers.getMapper(BoardMapper.class);

    @Mapping(source = "user.name", target = "author")
    @Mapping(source = "route.startAt", target = "startAt", dateFormat = "yyyy-MM-dd")
    @Mapping(source = "route.endAt", target = "endAt", dateFormat = "yyyy-MM-dd")
    @Mapping(source = "postImages", target = "representativeImage", qualifiedByName = "firstImageUrl")
    BoardAllDto boardEntityToBoardAllDto(BoardEntity boardEntity);

    @Named("firstImageUrl")
    default String mapFirstImageUrl(List<PostImageEntity> postImages) {
        return postImages != null && !postImages.isEmpty() ? postImages.get(0).getUrl() : null;
    }
}
