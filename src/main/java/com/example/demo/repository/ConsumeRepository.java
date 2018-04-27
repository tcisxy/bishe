package com.example.demo.repository;

import com.example.demo.entity.Consume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

public interface ConsumeRepository extends JpaRepository<Consume,Long>,JpaSpecificationExecutor<Consume> {
    Consume findById(long id);

    List<Consume> findByUserId(long userId);

    @Query("update Consume set status = ?2 where id = ?1")
    @Modifying
    @Transactional
    void editConsumeById(Long id, Integer status);

    @Query("select sum(money) from Consume where userId = ?1")
    Long sumMoneyByUserId(long id);

    List<Consume> findAllByOrderByTime();

    List<Consume> findAllByOrderByTimeDesc();
}
