package com.example.demo.service.impl;

import com.example.demo.entity.Appointment;
import com.example.demo.entity.User;
import com.example.demo.repository.AppointmentRepository;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Appointment> getAppointList() {
        List<Appointment> appointments = appointmentRepository.findAll();
        for(Appointment appointment : appointments) {
            appointment.setDoctorName(doctorRepository.findById(appointment.getDoctorId()).get().getName());
            appointment.setPhone(userRepository.findById(appointment.getUserId()).get().getPhone());
            appointment.setUserName(userRepository.findById(appointment.getUserId()).get().getName());
        }
        return appointments;
    }

    @Override
    public List<Appointment> getAppointmentByPhone(String phone) {
        User user = userRepository.findByPhone(phone);
        List<Appointment> appointments = new ArrayList<>();
        if(user != null) {
            String _name = user.getName();
            String _phone = user.getPhone();
            appointments = appointmentRepository.findByUserId(user.getId());
            for(Appointment appointment : appointments) {
                appointment.setUserName(_name);
                appointment.setPhone(_phone);
                appointment.setDoctorName(doctorRepository.findById(appointment.getDoctorId()).get().getName());
            }
            return appointments;
        }
        return appointments;
    }

    @Override
    public Appointment getAppointmentById(long id) {
        Appointment appointment = appointmentRepository.findById(id);
        appointment.setDoctorName(doctorRepository.findById(appointment.getDoctorId()).get().getName());
        appointment.setUserName(userRepository.findById(appointment.getUserId()).get().getName());
        appointment.setPhone(userRepository.findById(appointment.getUserId()).get().getPhone());
        return appointment;
    }

    @Override
    public void save(Appointment appointment) {
        if(appointment.getAppointStatus() == null) {
            appointment.setAppointStatus(0);
        }
        if(appointment.getTime() == null) {
            appointment.setTime(new Timestamp(System.currentTimeMillis()));
        }
        if(appointment.getPhone() != null) {
            User user = userRepository.findByPhone(appointment.getPhone());
            appointment.setUserId(user.getId());
        }
        appointmentRepository.save(appointment);
    }

    @Override
    public void edit(Appointment appointment) {
        appointmentRepository.edit(appointment.getId(),appointment.getAppointStatus());
    }
}
