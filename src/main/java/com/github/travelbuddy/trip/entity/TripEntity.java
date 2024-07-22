package com.github.travelbuddy.trip.entity;

import com.github.travelbuddy.board.entity.BoardEntity;
import com.github.travelbuddy.users.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "trips")
@ToString
public class TripEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id" , nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "post_id" , nullable = false)
    private BoardEntity board;

    @Column(name = "age_min", nullable = false)
    private Integer ageMin;

    @Column(name = "age_max", nullable = false)
    private Integer ageMax;

    @Column(name = "target_number", nullable = false)
    private Integer targetNumber;

    @Column(name = "participant_count", nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer participantCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    public enum Gender {
        MALE, FEMALE, ALL
    }

}
