package com.github.travelbuddy.likes.response;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class LikeInfoResponse {
    private Integer count;
    private boolean isLike;
}
