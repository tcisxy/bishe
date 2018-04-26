package com.example.demo.service.impl;

import com.example.demo.component.PayComponent;
import com.example.demo.entity.Recharge;
import com.example.demo.entity.User;
import com.example.demo.entity.VipLevel;
import com.example.demo.repository.RechargeRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.VipLevelRepository;
import com.example.demo.service.RechargeService;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class RechargeServiceImpl implements RechargeService {

    @Autowired
    private RechargeRepository rechargeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VipLevelRepository vipLevelRepository;
    @Autowired
    private PayComponent payComponent;

    @Override
    public List<Recharge> getRechargeList() {
        List<Recharge> recharges = rechargeRepository.findAll();
        for (Recharge recharge : recharges) {
            User user = userRepository.findById(recharge.getUserId());
            recharge.setPhone(user.getPhone());
            recharge.setUserName(user.getName());
            recharge.setPayName(payComponent.getType().get(recharge.getType()));
        }
        return recharges;
    }

    @Override
    public List<Recharge> getRechargeListByParam(Recharge recharge) {
        List<Recharge> recharges = new ArrayList<>();
        if(!StringUtils.isEmpty(recharge.getPhone())) {
            User user = userRepository.findByPhone(recharge.getPhone());
            if(user != null) {
                if(recharge.getStartTime() != null) {
                    if(recharge.getEndTime() != null) {
                        recharges = rechargeRepository.findByStartTimeAndEndTimeAndUserId(recharge.getStartTime(),recharge.getEndTime(),user.getId());
                    }else{
                        recharges = rechargeRepository.findByStartTimeAndEndTimeAndUserId(recharge.getStartTime(), new Timestamp(System.currentTimeMillis()), user.getId());
                    }
                }else {
                    if(recharge.getEndTime() != null) {
                        recharges = rechargeRepository.findByStartTimeAndEndTimeAndUserId(new Timestamp(0L),recharge.getEndTime(),user.getId());
                    }else{
                        recharges = rechargeRepository.findByUserId(user.getId());
                    }
                }
            }
        }else {
            if(recharge.getStartTime() != null) {
                if(recharge.getEndTime() != null) {
                    recharges = rechargeRepository.findByStartTimeAndEndTime(recharge.getStartTime(),recharge.getEndTime());
                }else {
                    recharges = rechargeRepository.findByStartTimeAndEndTime(recharge.getStartTime(),new Timestamp(System.currentTimeMillis()));
                }
            }else{
                if(recharge.getEndTime() != null) {
                    recharges = rechargeRepository.findByStartTimeAndEndTime(new Timestamp(0L),recharge.getEndTime());
                }
            }
        }

        for (Recharge _recharge : recharges) {
            User _user = userRepository.findById(_recharge.getUserId());
            _recharge.setPhone(_user.getPhone());
            _recharge.setUserName(_user.getName());
        }

        return recharges;
    }

    @Override
    public Recharge findRechargeById(long id) {
        Recharge recharge = rechargeRepository.findById(id);
        User user = userRepository.findById(recharge.getUserId());
        recharge.setUserName(user.getName());
        recharge.setPhone(user.getPhone());
        return recharge;
    }

    @Override
    public void save(Recharge recharge) {
        User user = userRepository.findByPhone(recharge.getPhone());
        if (user == null) {
            return;
        }
        if (recharge.getRechargeStatus() == null)
            recharge.setRechargeStatus(0);
        recharge.setUserId(user.getId());
        recharge.setTime(new Timestamp(System.currentTimeMillis()));
        user.setCurMoney(user.getCurMoney() + recharge.getMoney());
        user.setTotalRecharge(user.getTotalRecharge() + recharge.getMoney());
        user.setVipLevel(getVipLevel(user.getTotalRecharge()));
        rechargeRepository.save(recharge);
    }

    @Override
    public void edit(Recharge recharge) {
        rechargeRepository.save(recharge);
    }

    @Override
    public void delete(long id) {
        Recharge recharge = rechargeRepository.findById(id);
        User user = userRepository.findById(recharge.getUserId());
        user.setTotalRecharge(user.getTotalRecharge() - recharge.getMoney());
        user.setCurMoney(user.getCurMoney() - recharge.getMoney());
        user.setVipLevel(getVipLevel(user.getTotalRecharge()));
        rechargeRepository.deleteById(id);
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
