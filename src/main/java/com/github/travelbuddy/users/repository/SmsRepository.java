package com.github.travelbuddy.users.repository;

import com.github.travelbuddy.users.entity.SmsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsRepository extends JpaRepository<SmsEntity, Integer> {
    String findByPhoneNum(String phoneNum);
}
