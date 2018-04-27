package com.example.demo.repository;

import com.example.demo.entity.Recharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

public interface RechargeRepository extends JpaRepository<Recharge,Long> {
    Recharge findById(long id);

    @Query("select sum(money) from Recharge where userId = ?1")
    Long sumMoneyByUserId(long userId);

    List<Recharge> findByUserId(long userId);

    @Query("select r from Recharge r where r.time between ?1 and ?2")
    List<Recharge> findByStartTimeAndEndTime(Timestamp startTime, Timestamp endTime);

    @Query("select r from Recharge r where r.userId = ?3 and r.time between ?1 and ?2")
    List<Recharge> findByStartTimeAndEndTimeAndUserId(Timestamp startTime, Timestamp endTime, long userId);
}
