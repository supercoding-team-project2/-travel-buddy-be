package com.github.travelbuddy.comment.response;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AllCommentResponse {
    private Integer postId;
    private String message;
    private List<CommentResponse> commentList;
}
