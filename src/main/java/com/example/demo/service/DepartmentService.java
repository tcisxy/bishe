package com.example.demo.service;

import com.example.demo.entity.Department;

import java.util.List;

public interface DepartmentService {
    Department getDepById(Long id);

    List<Department> getDepList();

    void save(Department department);

    void delete(Long id);
}
