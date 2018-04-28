package com.example.demo.service;

import com.example.demo.entity.Doctor;
import com.example.demo.param.QueryParam;
import org.springframework.transaction.annotation.Transactional;

import javax.print.Doc;
import java.sql.Timestamp;
import java.util.List;

public interface DoctorService {
    List<Doctor> getDoctorList();

    Doctor getDoctorById(long id);

    void save(Doctor doctor);

    void edit(Doctor doctor);

    void delete(long id);

    @Transactional
    void checkDoctorInfo();

    long count();

    List<Doctor> getAvailableDoctorList(Timestamp startTime, Timestamp endTime);

    List<Doctor> getDoctorListByParam(QueryParam queryParam);
}
