package com.example.demo.service.impl;

import com.example.demo.entity.User;
import com.example.demo.entity.VipLevel;
import com.example.demo.repository.*;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RechargeRepository rechargeRepository;
    @Autowired
    private ConsumeRepository consumeRepository;
    @Autowired
    private VipLevelRepository vipLevelRepository;
    @Autowired
    private VisitRepository visitRepository;

    @Override
    public List<User> getUserList() {
        return userRepository.findAll();
    }

    @Override
    public void save(User user) {
        if (user.getSex() == null) {
            user.setSex(1);
        }
        if (user.getAge() == null) {
            user.setAge(0);
        }
        if(user.getCurMoney() == null) {
            user.setCurMoney(0L);
        }
        if(user.getTotalRecharge() == null) {
            user.setTotalRecharge(0L);
        }
        if(user.getTotalConsume() == null) {
            user.setTotalConsume(0L);
        }
        if(user.getVisitNum() == null) {
            user.setVisitNum(0);
        }
        if(user.getVipLevel() == null) {
            user.setVipLevel(0);
        }
        userRepository.save(user);
    }

    @Override
    public User getUserByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public void edit(User user) {
        userRepository.editUser(user.getId(), user.getName(), user.getAge(), user.getSex(), user.getPhone());
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void checkUserMoney() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            Long recharge = rechargeRepository.sumMoneyByUserId(user.getId());
            Long consume = consumeRepository.sumMoneyByUserId(user.getId());
            user.setTotalRecharge(recharge == null ? 0 : recharge);
            user.setTotalConsume(consume == null ? 0 : consume);
            user.setCurMoney(user.getTotalRecharge() - user.getTotalConsume());
            user.setVipLevel(getVipLevel(user.getTotalRecharge()));
            user.setVisitNum(visitRepository.countByUserId(user.getId()));
            userRepository.save(user);
        }
    }

    private int getVipLevel(long rechargeMoney) {
        long stand = 0;
        long id = 0;
        List<VipLevel> vipLevels = vipLevelRepository.findAll();
        for (VipLevel vipLevel : vipLevels) {
            if (vipLevel.getLevelStandard() <= rechargeMoney && vipLevel.getLevelStandard() >= stand) {
                stand = vipLevel.getLevelStandard();
                id = vipLevel.getId();
            }
        }
        return (int) id;
    }
}
