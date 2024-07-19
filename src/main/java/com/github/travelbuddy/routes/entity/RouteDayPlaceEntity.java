package com.github.travelbuddy.routes.entity;

import com.github.travelbuddy.routes.enums.PlaceCategory;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "routes_day_place")
public class RouteDayPlaceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "routes_day_id", nullable = false)
    private RouteDayEntity routeDay;

    @Column(name = "place_name", nullable = false)
    private String placeName;

    @Enumerated(EnumType.STRING)
    @Column(name = "place_category", nullable = false)
    private PlaceCategory placeCategory;
}