package com.example.demo.repository;

import com.example.demo.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface DoctorRepository extends JpaRepository<Doctor,Long>,JpaSpecificationExecutor<Doctor> {
    Doctor findById(long id);

    @Query("update Doctor set name = ?2,age = ?3,sex = ?4,phone = ?5,description = ?6,position = ?7,price = ?8 where id = ?1")
    @Modifying
    @Transactional
    void editDoctor(long id, String name, int age, int sex, String phone, String description, String position, long price);
}
