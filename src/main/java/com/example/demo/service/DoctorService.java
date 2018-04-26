package com.example.demo.service;

import com.example.demo.entity.Doctor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DoctorService {
    List<Doctor> getDoctorList();

    Doctor getDoctorById(long id);

    void save(Doctor doctor);

    void edit(Doctor doctor);

    void delete(long id);

    @Transactional
    void checkDoctorInfo();
}
