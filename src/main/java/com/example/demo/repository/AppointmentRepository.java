package com.example.demo.repository;

import com.example.demo.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment,Long>,JpaSpecificationExecutor<Appointment> {
    Appointment findById(long id);

    List<Appointment> findByUserId(long id);

    @Query("update Appointment set status = ?2 where id = ?1")
    @Modifying
    @Transactional
    void edit(Long id,Integer status);

    @Query("select count(Appointment ) from Appointment where time between ?1 and ?2")
    long countToday(Timestamp start, Timestamp end);
}
