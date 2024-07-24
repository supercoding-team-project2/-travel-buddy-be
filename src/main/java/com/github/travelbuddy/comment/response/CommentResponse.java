package com.github.travelbuddy.comment.response;

import com.github.travelbuddy.comment.entity.CommentEntity;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {
    private String comment;
    private String userName;
    private String profileImgUrl;
}
