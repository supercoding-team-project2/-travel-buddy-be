package com.github.travelbuddy.users.repository;

import com.github.travelbuddy.users.entity.SmsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsRepository extends JpaRepository<SmsEntity, Integer> {
    @Query("SELECT code FROM SmsEntity WHERE phoneNum= :phoneNum")
    String findByPhoneNum(String phoneNum);
    void deleteByPhoneNum(String phoneNum);
}
