package com.example.demo.service.impl;

import com.example.demo.component.PayComponent;
import com.example.demo.entity.Consume;
import com.example.demo.entity.User;
import com.example.demo.repository.ConsumeRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ConsumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

    @Override
    public List<Consume> getConsumeList() {
        List<Consume> list = consumeRepository.findAll();
        for(Consume consume : list) {
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
    public List<Consume> getConsumeListByParam(Consume consume) {
        List<Consume> consumes = new ArrayList<>();
        if(!StringUtils.isEmpty(consume.getPhone())) {
            User user = userRepository.findByPhone(consume.getPhone());
            if(user != null) {
                if(consume.getStartTime() != null) {
                    if(consume.getEndTime() != null) {
                        consumes = consumeRepository.findByStartTimeAndEndTimeAndUserId(consume.getStartTime(),consume.getEndTime(),user.getId());
                    }else{
                        consumes = consumeRepository.findByStartTimeAndEndTimeAndUserId(consume.getStartTime(), new Timestamp(System.currentTimeMillis()), user.getId());
                    }
                }else {
                    if(consume.getEndTime() != null) {
                        consumes = consumeRepository.findByStartTimeAndEndTimeAndUserId(new Timestamp(0L),consume.getEndTime(),user.getId());
                    }else{
                        consumes = consumeRepository.findByUserId(user.getId());
                    }
                }
            }
        }else {
            if(consume.getStartTime() != null) {
                if(consume.getEndTime() != null) {
                    consumes = consumeRepository.findByStartTimeAndEndTime(consume.getStartTime(),consume.getEndTime());
                }else {
                    consumes = consumeRepository.findByStartTimeAndEndTime(consume.getStartTime(),new Timestamp(System.currentTimeMillis()));
                }
            }else{
                if(consume.getEndTime() != null) {
                    consumes = consumeRepository.findByStartTimeAndEndTime(new Timestamp(0L),consume.getEndTime());
                }
            }
        }

        for (Consume _consume : consumes) {
            User _user = userRepository.findById(_consume.getUserId());
            _consume.setPhone(_user.getPhone());
            _consume.setUserName(_user.getName());
        }

        return consumes;
    }

    @Override
    public void save(Consume consume) {
        if(consume.getConsumeStatus() == null)
            consume.setConsumeStatus(0);
        User user = userRepository.findByPhone(consume.getPhone());
        consume.setUserId(user.getId());
        consume.setTime(new Timestamp(System.currentTimeMillis()));
        user.setTotalConsume(user.getTotalConsume() + consume.getMoney());
        user.setCurMoney(user.getCurMoney() - consume.getMoney());
        consumeRepository.save(consume);
    }

    @Override
    public void edit(Consume consume) {
        consumeRepository.editConsumeById(consume.getId(),consume.getConsumeStatus());
    }

    @Override
    public void delete(long id) {
        Consume consume = consumeRepository.findById(id);
        User user = userRepository.findById(consume.getUserId());
        user.setCurMoney(user.getCurMoney() + consume.getMoney());
        user.setTotalConsume(user.getTotalConsume() - consume.getMoney());
        consumeRepository.deleteById(id);
    }

}
