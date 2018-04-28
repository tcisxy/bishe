package com.example.demo.service;

import com.example.demo.entity.Consume;
import com.example.demo.param.QueryParam;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ConsumeService {
    List<Consume> getConsumeList();

    Consume findConsumeById(long id);

    List<Consume> getConsumeListByParam(QueryParam queryParam);

    Long sumMoneyByParam(QueryParam queryParam);

    @Transactional
    Consume save(Consume consume);

    void edit(Consume consume);

    @Transactional
    void delete(long id);

    List<Consume> getConsumeListOrderByTime();
}
