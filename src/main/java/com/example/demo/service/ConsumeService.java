package com.example.demo.service;

import com.example.demo.entity.Consume;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ConsumeService {
    List<Consume> getConsumeList();

    Consume findConsumeById(long id);

    List<Consume> getConsumeListByParam(Consume consume);

    @Transactional
    void save(Consume consume);

    void edit(Consume consume);

    @Transactional
    void delete(long id);
}
