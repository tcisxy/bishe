package com.example.demo.repository;

import com.example.demo.entity.VipLevel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VipLevelRepository extends JpaRepository<VipLevel,Long> {
    VipLevel findById(long id);
}
