package com.example.demo.service.impl;

import com.example.demo.entity.Doctor;
import com.example.demo.entity.User;
import com.example.demo.entity.Visit;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.VisitRepository;
import com.example.demo.service.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.List;

@Service
public class VisitServiceImpl implements VisitService {

    @Autowired
    private VisitRepository visitRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DoctorRepository doctorRepository;

    @Override
    public List<Visit> getUserVisitList() {
        List<Visit> visits = visitRepository.findAll();
        for(Visit visit : visits) {
            visit.setUserName(userRepository.findById(visit.getUserId()).get().getName());
            visit.setDoctorName(doctorRepository.findById(visit.getDoctorId()).get().getName());
            visit.setPhone(userRepository.findById(visit.getUserId()).get().getPhone());
        }
        return visits;
    }

    @Override
    public Visit findById(long id) {
        Visit visit = visitRepository.findById(id);
        visit.setUserName(userRepository.findById(visit.getUserId()).get().getName());
        visit.setDoctorName(doctorRepository.findById(visit.getDoctorId()).get().getName());
        visit.setPhone(userRepository.findById(visit.getUserId()).get().getPhone());
        return visit;
    }

    @Override
    public void save(Visit visit) {
        if(!StringUtils.isEmpty(visit.getPhone())) {
            User user = userRepository.findByPhone(visit.getPhone());
            visit.setUserId(user.getId());
            user.setVisitNum(user.getVisitNum() + 1);
        }
        Doctor doctor = doctorRepository.findById(visit.getDoctorId()).get();
        doctor.setDealNum(doctor.getDealNum() + 1);
        visit.setTime(new Timestamp(System.currentTimeMillis()));
        visit.setStatus(0);
        visit.setFee(0L);
        visitRepository.save(visit);
    }

    @Override
    public void edit(Visit visit) {
        if(visit.getFee() == null)
            visit.setFee(0L);
        if(visit.getTime() == null)
            visit.setTime(new Timestamp(System.currentTimeMillis()));
        visitRepository.editVisit(visit.getId(),visit.getTime(),visit.getDescription(),visit.getSuggestion(),visit.getFee());
    }

    @Override
    public void delete(long id) {
        Visit visit = visitRepository.findById(id);
        User user = userRepository.findById(visit.getUserId()).get();
        user.setVisitNum(user.getVisitNum() - 1);
        Doctor doctor = doctorRepository.findById(visit.getDoctorId()).get();
        doctor.setDealNum(doctor.getDealNum() - 1);
        visitRepository.delete(visit);
    }
}
