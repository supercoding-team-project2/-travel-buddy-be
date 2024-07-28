package com.github.travelbuddy.comment.response;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AddCommentResponse {
    private Integer postId;
    private String message;
}
