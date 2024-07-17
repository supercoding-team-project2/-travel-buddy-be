package com.github.travelbuddy.board.entity;

import com.github.travelbuddy.postImage.entity.PostImageEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "boards")
@ToString
public class BoardEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // TODO : UserEntity 작성 후 수정
    @Column(name = "user_id")
    private Integer userId;

    // TODO : RouteEntity 작성 후 수정
    @Column(name = "route_id")
    private Integer routeId;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 255)
    private String summary;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column(columnDefinition = "int default 0")
    private Integer suggestion;

    @Column(name = "created_at",nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "board")
    private List<PostImageEntity> postImages;

    public enum Category {
        REVIEW, COMPANION, GUIDE
    }
}
