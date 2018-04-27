package com.example.demo.service.impl;

import com.example.demo.entity.Appointment;
import com.example.demo.entity.User;
import com.example.demo.entity.Visit;
import com.example.demo.param.QueryParam;
import com.example.demo.repository.AppointmentRepository;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.VisitRepository;
import com.example.demo.service.AppointmentService;
import com.example.demo.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VisitRepository visitRepository;

    @Override
    public List<Appointment> getAppointList() {
        List<Appointment> appointments = appointmentRepository.findAll();
        for (Appointment appointment : appointments) {
            appointment.setDoctorName(doctorRepository.findById(appointment.getDoctorId()).get().getName());
            appointment.setPhone(userRepository.findById(appointment.getUserId()).get().getPhone());
            appointment.setUserName(userRepository.findById(appointment.getUserId()).get().getName());
        }
        return appointments;
    }

    @Override
    public List<Appointment> getAppointmentByParam(QueryParam queryParam) {
        List<Appointment> appointments = appointmentRepository.findAll(getWhereClause(queryParam.getPhone(), queryParam.getStartTime(), queryParam.getEndTime()), new Sort(Sort.Direction.DESC, "time"));
        for (Appointment _appointment : appointments) {
            _appointment.setUserName(userRepository.findById(_appointment.getUserId()).get().getName());
            _appointment.setPhone(userRepository.findById(_appointment.getUserId()).get().getPhone());
            _appointment.setDoctorName(doctorRepository.findById(_appointment.getDoctorId()).get().getName());
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
        if (appointment.getAppointStatus() == null) {
            appointment.setAppointStatus(0);
        }
        if (appointment.getTime() == null) {
            appointment.setTime(new Timestamp(System.currentTimeMillis()));
        }
        if (appointment.getPhone() != null) {
            User user = userRepository.findByPhone(appointment.getPhone());
            appointment.setUserId(user.getId());
        }
        appointmentRepository.save(appointment);
    }

    @Override
    public void edit(Appointment appointment) {
        appointmentRepository.edit(appointment.getId(), appointment.getAppointStatus());
    }

    @Override
    public long count() {
        return appointmentRepository.countToday(TimeUtil.getTodayStartTime(), TimeUtil.getTodayEndTime());
    }

    @Override
    public boolean queue(long id) {
        Appointment appointment = appointmentRepository.findById(id);
        if(appointment.getTime().after(new Timestamp(System.currentTimeMillis()))) {
            return false;
        }
        appointment.setAppointStatus(1);

        Visit visit = new Visit();
        visit.setDoctorId(appointment.getDoctorId());
        visit.setPhone(appointment.getPhone());
        visit.setUserId(appointment.getUserId());
        visit.setStatus(0);
        visit.setFee(doctorRepository.findById(appointment.getDoctorId()).get().getPrice());
        appointment.setVisitId(visitRepository.save(visit).getId());
        appointmentRepository.save(appointment);
        return true;
    }

    private Specification<Appointment> getWhereClause(final String phone, final Timestamp startTime, final Timestamp endTime) {
        return new Specification<Appointment>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<Appointment> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.conjunction();
                if (phone != null) {
                    User user = userRepository.findByPhone(phone);
                    if (user != null)
                        predicate.getExpressions().add(criteriaBuilder.equal(root.<Long>get("userId"), user.getId()));
                }

                if (startTime != null && endTime != null)
                    predicate.getExpressions().add(criteriaBuilder.between(root.<Timestamp>get("time"), startTime, endTime));
                else if (startTime != null && endTime == null)
                    predicate.getExpressions().add(criteriaBuilder.greaterThanOrEqualTo(root.<Timestamp>get("time"), startTime));
                else if (startTime == null && endTime != null)
                    predicate.getExpressions().add(criteriaBuilder.lessThanOrEqualTo(root.<Timestamp>get("time"), endTime));

                return predicate;
            }
        };
    }
}
