package com.github.travelbuddy.comment.response;

import lombok.*;

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
