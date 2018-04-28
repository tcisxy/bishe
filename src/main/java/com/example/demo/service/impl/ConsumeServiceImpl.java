package com.example.demo.service.impl;

import com.example.demo.component.PayComponent;
import com.example.demo.entity.Consume;
import com.example.demo.entity.User;
import com.example.demo.entity.VipLevel;
import com.example.demo.param.QueryParam;
import com.example.demo.repository.ConsumeRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.VipLevelRepository;
import com.example.demo.service.ConsumeService;
import com.example.demo.util.IndexUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
public class ConsumeServiceImpl implements ConsumeService {

    @Autowired
    private ConsumeRepository consumeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PayComponent payComponent;
    @Autowired
    private VipLevelRepository vipLevelRepository;
    @Autowired
    private UserServiceImpl userServiceImpl;

    @Override
    public List<Consume> getConsumeList() {
        List<Consume> list = consumeRepository.findAllByOrderByTimeDesc();
        for (Consume consume : list) {
            User user = userRepository.findById(consume.getUserId());
            consume.setUserName(user.getName());
            consume.setPhone(user.getPhone());
            consume.setPayName(payComponent.getType().get(consume.getType()));
        }
        return list;
    }

    @Override
    public Consume findConsumeById(long id) {
        Consume consume = consumeRepository.findById(id);
        consume.setUserName(userRepository.findById(consume.getUserId()).getName());
        return consume;
    }

    @Override
    public List<Consume> getConsumeListByParam(QueryParam queryParam) {
        List<Consume> consumes = consumeRepository.findAll(getWhereClause(queryParam.getName(),queryParam.getPhone(),queryParam.getStartTime(),queryParam.getEndTime()),new Sort(Sort.Direction.DESC,"time"));
        for(Consume _consume : consumes) {
            User user = userRepository.findById(_consume.getUserId());
            _consume.setPhone(user.getPhone());
            _consume.setUserName(user.getName());
            _consume.setPayName(payComponent.getType().get(_consume.getType()));
        }
        return consumes;
    }

    @Override
    public Long sumMoneyByParam(QueryParam queryParam) {
        long sum = 0;
        for(Consume consume : getConsumeListByParam(queryParam)) {
            sum += consume.getMoney();
        }
        return sum;
    }

    @Override
    public Consume save(Consume consume) {
        if (consume.getConsumeStatus() == null)
            consume.setConsumeStatus(0);
        User user;
        if(consume.getPhone() != null) {
            user = userRepository.findByPhone(consume.getPhone());
            consume.setUserId(user.getId());
        }else {
            user = userRepository.findById(consume.getUserId());
        }

        if (consume.getTime() == null)
            consume.setTime(new Timestamp(System.currentTimeMillis()));
        user.setTotalConsume(user.getTotalConsume() + consume.getMoney());
        user.setVipLevel(IndexUtil.getVipLevel(vipLevelRepository.findAll(),user.getTotalConsume()));
        return consumeRepository.save(consume);
    }

    @Override
    public void edit(Consume consume) {
        consumeRepository.editConsumeById(consume.getId(), consume.getConsumeStatus());
    }

    @Override
    public void delete(long id) {
        Consume consume = consumeRepository.findById(id);
        User user = userRepository.findById(consume.getUserId());
        user.setTotalConsume(user.getTotalConsume() - consume.getMoney());
        user.setVipLevel(IndexUtil.getVipLevel(vipLevelRepository.findAll(),user.getTotalConsume()));
        consumeRepository.deleteById(id);
    }

    @Override
    public List<Consume> getConsumeListOrderByTime() {
        return consumeRepository.findAllByOrderByTime();
    }

    private Specification<Consume> getWhereClause(final String name, final String phone,final Timestamp startTime,final Timestamp endTime) {
        return new Specification<Consume>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<Consume> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.conjunction();
                if(!StringUtils.isEmpty(name) || !StringUtils.isEmpty(phone)) {
                    List<User> users = userRepository.findAll(userServiceImpl.getWhereClause(name,phone));
                    if(users.size() == 0) {
                        predicate.getExpressions().add(criteriaBuilder.equal(root.<Long>get("userId"),-1));
                    }else {
                        List<Long> userIds = new ArrayList<>();
                        for(User user : users) {
                            userIds.add(user.getId());
                        }
                        predicate.getExpressions().add(criteriaBuilder.and(root.<Long>get("userId").in(userIds)));
                    }
                }

                if(startTime != null && endTime != null)
                    predicate.getExpressions().add(criteriaBuilder.between(root.<Timestamp>get("time"),startTime,endTime));
                else if(startTime != null && endTime == null)
                    predicate.getExpressions().add(criteriaBuilder.greaterThanOrEqualTo(root.<Timestamp>get("time"),startTime));
                else if(startTime == null && endTime != null)
                    predicate.getExpressions().add(criteriaBuilder.lessThanOrEqualTo(root.<Timestamp>get("time"),endTime));

                return predicate;
            }
        };
    }
}
