package com.example.demo.service.impl;

import com.example.demo.entity.Doctor;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.VisitRepository;
import com.example.demo.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private VisitRepository visitRepository;

    @Override
    public List<Doctor> getDoctorList() {
        return doctorRepository.findAll();
    }

    @Override
    public Doctor getDoctorById(long id) {

        return doctorRepository.findById(id);
    }

    @Override
    public void save(Doctor doctor) {
        if(doctor.getAge() == null) {
            doctor.setAge(0);
        }
        if(doctor.getSex() == null) {
            doctor.setSex(1);
        }
        doctor.setDealNum(0);
        doctor.setPrice(0L);
        doctorRepository.save(doctor);
    }

    @Override
    public void edit(Doctor doctor) {
        if(doctor.getAge() == null)
            doctor.setAge(0);
        if(doctor.getSex() == null)
            doctor.setSex(1);
        if(doctor.getPrice() == null)
            doctor.setPrice(0L);
        doctorRepository.editDoctor(doctor.getId(),doctor.getName(),doctor.getAge(),doctor.getSex(),doctor.getPhone(),doctor.getDescription(),doctor.getPosition(),doctor.getPrice());
    }

    @Override
    public void delete(long id) {
        doctorRepository.deleteById(id);
    }

    @Override
    public void checkDoctorInfo() {
        List<Doctor> doctors = doctorRepository.findAll();
        for(Doctor doctor : doctors) {
            doctor.setDealNum(visitRepository.countByDoctorId(doctor.getId()));
            doctorRepository.save(doctor);
        }
    }
}
