package com.github.travelbuddy.likes.entity;

import com.github.travelbuddy.board.entity.BoardEntity;
import com.github.travelbuddy.users.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "likes")
@ToString
public class LikesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private BoardEntity board;
}
