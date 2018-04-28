package com.example.demo.service;

import com.example.demo.entity.Employee;

import java.util.List;

public interface EmployeeService {
    List<Employee> getEmpList();

    void save(Employee employee);

    List<Employee> checkAccount(String account,String password);

    void edit(Employee employee);

    void delete(Long id);

    Employee getEmpById(Long id);

    List<Employee> getEmpListByName(String name);
}
