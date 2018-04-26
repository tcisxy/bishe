package com.example.demo.service;

import com.example.demo.entity.Visit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface VisitService {
    List<Visit> getUserVisitList();

    Visit findById(long id);

    @Transactional
    void save(Visit visit);

    void edit(Visit visit);

    @Transactional
    void delete(long id);
}
