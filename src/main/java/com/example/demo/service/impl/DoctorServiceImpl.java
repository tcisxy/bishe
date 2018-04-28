package com.example.demo.service.impl;

import com.example.demo.entity.Doctor;
import com.example.demo.param.QueryParam;
import com.example.demo.repository.AppointmentRepository;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.VisitRepository;
import com.example.demo.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private VisitRepository visitRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;

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

    @Override
    public long count() {
        return doctorRepository.count();
    }

    @Override
    public List<Doctor> getAvailableDoctorList(Timestamp startTime, Timestamp endTime) {
        List<Long> appointedDoctors = appointmentRepository.findAppointedDoctor(startTime,endTime);
        List<Doctor> availableDoctors = new ArrayList<>();
        for(Doctor doctor : doctorRepository.findAll()) {
            if(!appointedDoctors.contains(doctor.getId())) {
                availableDoctors.add(doctor);
            }
        }
        return availableDoctors;
    }

    @Override
    public List<Doctor> getDoctorListByParam(QueryParam queryParam) {
        return doctorRepository.findAll(getWhereClause(queryParam.getName(),queryParam.getPhone()));
    }

    private Specification<Doctor> getWhereClause(final String name, final String phone) {
        return new Specification<Doctor>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<Doctor> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.conjunction();
                if(!StringUtils.isEmpty(name))
                    predicate.getExpressions().add(criteriaBuilder.equal(root.<String>get("name"),name));
                if(!StringUtils.isEmpty(phone))
                    predicate.getExpressions().add(criteriaBuilder.equal(root.<String>get("phone"),phone));
                return predicate;
            }
        };
    }


}
