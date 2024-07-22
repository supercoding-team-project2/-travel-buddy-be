package com.github.travelbuddy.users.entity;

import com.github.travelbuddy.users.enums.Gender;
import com.github.travelbuddy.users.enums.Role;
import com.github.travelbuddy.users.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class UserEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", length = 30, nullable = false)
    private String name;

    @Column(name = "email", length = 40, nullable = false, unique = true)
    private String email;

    @Column(name = "password", length = 100, nullable = false)
    private String password;

    @Column(name = "resident_num", length = 20, nullable = false)
    private String residentNum;

    @Column(name = "phone_num", length = 20, nullable = false, unique = true)
    private String phoneNum;

    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    //todo: 기본 사진
    @Column(name = "profile_picture_url", length = 255, columnDefinition = "VARCHAR(255) DEFAULT ''")
    private String profilePictureUrl;

    @Column(name = "status", columnDefinition = "ENUM('ACTIVE', 'DELETED') DEFAULT 'ACTIVE'")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "role", columnDefinition = "ENUM('USER', 'ALL') DEFAULT 'USER'")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
