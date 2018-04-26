package com.example.demo.service;

import com.example.demo.entity.Appointment;

import java.util.List;

public interface AppointmentService {
    List<Appointment> getAppointList();

    List<Appointment> getAppointmentByPhone(String phone);

    Appointment getAppointmentById(long id);

    void save(Appointment appointment);

    void edit(Appointment appointment);
}
