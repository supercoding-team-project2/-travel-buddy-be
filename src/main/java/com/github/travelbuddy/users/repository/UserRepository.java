package com.github.travelbuddy.users.repository;

import com.github.travelbuddy.users.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Boolean existsByEmail(String email);

    UserEntity findByEmail(String email);
}
