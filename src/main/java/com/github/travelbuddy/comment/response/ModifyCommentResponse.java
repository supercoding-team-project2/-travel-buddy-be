package com.github.travelbuddy.comment.response;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ModifyCommentResponse {
    private Integer postId;
    private String message;
    private CommentResponse commentResponse;
}
