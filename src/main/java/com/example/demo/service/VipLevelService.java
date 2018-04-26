package com.example.demo.service;

import com.example.demo.entity.VipLevel;

import java.util.List;

public interface VipLevelService {
    List<VipLevel> getVipLevelList();

    VipLevel getVipLevelById(long id);

    void save(VipLevel vipLevel);

    void edit(VipLevel vipLevel);

    void delete(long id);
}
