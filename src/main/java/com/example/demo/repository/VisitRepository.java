package com.example.demo.repository;


import com.example.demo.entity.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

public interface VisitRepository extends JpaRepository<Visit,Long> {
    Visit findById(long id);

    @Query("update Visit set time = ?2,description = ?3,suggestion = ?4,fee = ?5 where id = ?1")
    @Modifying
    @Transactional
    void editVisit(long id, Timestamp time, String description, String suggestion, long fee);

    @Query
    int countByUserId(long userId);

    @Query
    int countByDoctorId(long doctorId);
}
