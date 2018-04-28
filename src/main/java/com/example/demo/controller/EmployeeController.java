package com.example.demo.controller;

import com.example.demo.entity.Department;
import com.example.demo.entity.Employee;
import com.example.demo.service.DepartmentService;
import com.example.demo.service.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class EmployeeController {

    @Resource
    EmployeeService employeeService;
    @Resource
    DepartmentService departmentService;

    @RequestMapping("/list/emp")
    public ModelAndView employee(ModelAndView modelAndView) {
        List<Employee> employees = employeeService.getEmpList();
        modelAndView.addObject("emps",employees);
        modelAndView.setViewName("employee/list.html");
        return modelAndView;
    }

    @RequestMapping("/toAdd/emp")
    public ModelAndView toAdd(ModelAndView modelAndView) {
        List<Department> departments = departmentService.getDepList();
        modelAndView.addObject("depts",departments);
        modelAndView.setViewName("employee/addEmp.html");
        return modelAndView;
    }

    @RequestMapping("/add/emp")
    public ModelAndView add(ModelAndView modelAndView, Employee employee) {
        employee.setPassword(employee.getPassword().trim());
        if(employee.getPassword().length() < 6) {
            String message = "密码不能少于6位！";
            modelAndView.addObject("message",message);
            return toAdd(modelAndView);
        }
        employeeService.save(employee);
        modelAndView.setViewName("redirect:/list/emp");
        return modelAndView;
    }

    @RequestMapping("/toEdit/emp")
    public ModelAndView toEdit(Long id,ModelAndView modelAndView) {
        Employee employee = employeeService.getEmpById(id);
        modelAndView.addObject("emp",employee);
        List<Department> departments = departmentService.getDepList();
        modelAndView.addObject("depts",departments);
        modelAndView.setViewName("employee/editEmp.html");
        return modelAndView;
    }

    @RequestMapping("/edit/emp")
    public ModelAndView edit(ModelAndView modelAndView, Employee employee) {
        employee.setPassword(employee.getPassword().trim());
        if(employee.getPassword().length() < 6) {
            String message = "密码不能少于6位！";
            modelAndView.addObject("message",message);
            return toAdd(modelAndView);
        }
        employeeService.edit(employee);
        modelAndView.setViewName("redirect:/list/emp");
        return modelAndView;
    }

    @RequestMapping("/delete/emp")
    public ModelAndView delete(ModelAndView modelAndView, Long id) {
        employeeService.delete(id);
        modelAndView.setViewName("redirect:/list/emp");
        return modelAndView;
    }

    @RequestMapping("/query/emp")
    public ModelAndView query(ModelAndView modelAndView, Employee employee) {
        modelAndView.addObject("emps",employeeService.getEmpListByName(employee.getName()));
        modelAndView.addObject("name",employee.getName());
        modelAndView.setViewName("employee/list.html");
        return modelAndView;
    }
}
