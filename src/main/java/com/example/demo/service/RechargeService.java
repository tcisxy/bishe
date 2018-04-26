package com.example.demo.service;

import com.example.demo.entity.Recharge;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RechargeService {
    List<Recharge> getRechargeList();

    List<Recharge> getRechargeListByParam(Recharge recharge);

    Recharge findRechargeById(long id);

    @Transactional
    void save(Recharge recharge);

    void edit(Recharge recharge);

    @Transactional
    void delete(long id);
}
