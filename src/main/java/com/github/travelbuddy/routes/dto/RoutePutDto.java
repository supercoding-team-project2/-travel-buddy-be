package com.github.travelbuddy.routes.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoutePutDto {
    private String title;
    private String description;
}