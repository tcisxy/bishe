package com.example.demo.service.impl;

import com.example.demo.entity.Employee;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public List<Employee> getEmpList() {
        List<Employee> employees = employeeRepository.findAll();
        for(Employee employee : employees) {
            employee.setDeptName(departmentRepository.findById(employee.getDeptId()).getName());
        }
        return employees;
    }

    @Override
    public void save(Employee employee) {
        employeeRepository.save(employee);
    }

    @Override
    public List<Employee> checkAccount(String account, String password) {
        return employeeRepository.findByAccountAndPassword(account, password);
    }

    @Override
    public void edit(Employee employee) {
        employeeRepository.save(employee);
    }

    @Override
    public void delete(Long id) {
        employeeRepository.deleteById(id);
    }

    @Override
    public Employee getEmpById(Long id) {
        return employeeRepository.findById(id).get();
    }
}
