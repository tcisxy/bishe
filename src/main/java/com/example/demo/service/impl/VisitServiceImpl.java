package com.example.demo.service.impl;

import com.example.demo.entity.Consume;
import com.example.demo.entity.Doctor;
import com.example.demo.entity.User;
import com.example.demo.entity.Visit;
import com.example.demo.param.QueryParam;
import com.example.demo.repository.ConsumeRepository;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.VisitRepository;
import com.example.demo.service.VisitService;
import com.example.demo.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class VisitServiceImpl implements VisitService {

    @Autowired
    private VisitRepository visitRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private ConsumeRepository consumeRepository;

    @Override
    public List<Visit> getUserVisitList() {
        List<Visit> visits = visitRepository.findAllByOrderByTimeDesc();
        for (Visit visit : visits) {
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
    public Visit save(Visit visit) {
        if (!StringUtils.isEmpty(visit.getPhone())) {
            User user = userRepository.findByPhone(visit.getPhone());
            visit.setUserId(user.getId());
            user.setVisitNum(user.getVisitNum() + 1);
        }
        Doctor doctor = doctorRepository.findById(visit.getDoctorId()).get();
        doctor.setDealNum(doctor.getDealNum() + 1);
        if (visit.getTime() == null)
            visit.setTime(new Timestamp(System.currentTimeMillis()));
        if (visit.getTime() == null)
            visit.setStatus(0);
        if (visit.getFee() == null)
            visit.setFee(doctor.getPrice());
        return visitRepository.save(visit);
    }

    @Override
    public void edit(Visit visit) {
        if (visit.getFee() == null)
            visit.setFee(0L);
        if (visit.getTime() == null)
            visit.setTime(new Timestamp(System.currentTimeMillis()));
        visitRepository.editVisit(visit.getId(), visit.getTime(), visit.getDescription(), visit.getSuggestion(), visit.getFee());
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

    @Override
    public long count() {
        return visitRepository.countToday(TimeUtil.getTodayStartTime(), TimeUtil.getTodayEndTime());
    }

    @Override
    public List<Visit> queryByParam(QueryParam queryParam) {
        List<Visit> visits = visitRepository.findAll(getWhereClause(queryParam.getPhone(), queryParam.getStartTime(), queryParam.getEndTime()), new Sort(Sort.Direction.DESC, "time"));
        for (Visit _visit : visits) {
            _visit.setUserName(userRepository.findById(_visit.getUserId()).get().getName());
            _visit.setDoctorName(doctorRepository.findById(_visit.getDoctorId()).get().getName());
            _visit.setPhone(userRepository.findById(_visit.getUserId()).get().getPhone());
        }
        return visits;
    }

    private Specification<Visit> getWhereClause(final String phone, final Timestamp startTime, final Timestamp endTime) {
        return new Specification<Visit>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<Visit> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
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
