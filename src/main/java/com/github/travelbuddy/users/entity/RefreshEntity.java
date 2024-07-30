package com.github.travelbuddy.users.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "refreshes")
public class RefreshEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;
    @Column(length = 255)
    private String refresh;
    @Column(length = 30)
    private String expiration;
}
