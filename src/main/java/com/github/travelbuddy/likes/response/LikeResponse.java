package com.github.travelbuddy.likes.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LikeResponse {
    private Integer userId;
    private Integer boardId;
    private String message;
}
