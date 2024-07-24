package com.github.travelbuddy.comment.response;

import com.github.travelbuddy.comment.entity.CommentEntity;
import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {
    private int commentCount;
    private List<CommentEntity> comments;
}
