package com.example.demo.service.impl;

import com.example.demo.entity.Department;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService{

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public Department getDepById(Long id) {
        return departmentRepository.findById(id).get();
    }

    @Override
    public List<Department> getDepList() {
        return departmentRepository.findAll();
    }

    @Override
    public void save(Department department) {
        if(department.getAuth() == null)
            department.setAuth("");
        departmentRepository.save(department);
    }

    @Override
    public void delete(Long id) {
        departmentRepository.deleteById(id);
    }
}
