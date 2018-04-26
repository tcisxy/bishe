package com.example.demo.service.impl;

import com.example.demo.entity.VipLevel;
import com.example.demo.repository.VipLevelRepository;
import com.example.demo.service.VipLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VipLevelServiceImpl implements VipLevelService{

    @Autowired
    private VipLevelRepository vipLevelRepository;

    @Override
    public List<VipLevel> getVipLevelList() {
        return vipLevelRepository.findAll();
    }

    @Override
    public VipLevel getVipLevelById(long id) {
        return vipLevelRepository.findById(id);
    }

    @Override
    public void save(VipLevel vipLevel) {
        vipLevelRepository.save(vipLevel);
    }

    @Override
    public void edit(VipLevel vipLevel) {
        vipLevelRepository.save(vipLevel);
    }

    @Override
    public void delete(long id) {
        vipLevelRepository.deleteById(id);
    }
}
