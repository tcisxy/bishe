package com.example.demo.service;

import com.example.demo.entity.Appointment;
import com.example.demo.param.QueryParam;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AppointmentService {
    List<Appointment> getAppointList();

    List<Appointment> getAppointmentByParam(QueryParam queryParam);

    Appointment getAppointmentById(long id);

    void save(Appointment appointment);

    void edit(Appointment appointment);

    long count();

    @Transactional
    boolean queue(long id);
}
