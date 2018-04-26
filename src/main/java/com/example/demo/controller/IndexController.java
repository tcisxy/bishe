package com.example.demo.controller;

import com.example.demo.component.AuthComponent;
import com.example.demo.component.WebSecurityConfig;
import com.example.demo.entity.Department;
import com.example.demo.entity.Employee;
import com.example.demo.param.SideBar;
import com.example.demo.service.DepartmentService;
import com.example.demo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class IndexController {

    @Autowired
    AuthComponent authComponent;

    @Resource
    EmployeeService employeeService;
    @Resource
    DepartmentService departmentService;

    @RequestMapping("/index")
    public ModelAndView index(ModelAndView modelAndView) {
        modelAndView.setViewName("index.html");
        return modelAndView;
    }

    @RequestMapping("/")
    public ModelAndView home(ModelAndView modelAndView) {
        modelAndView.setViewName("redirect:/index");
        return modelAndView;
    }

    @RequestMapping("/login")
    public ModelAndView toLogin(ModelAndView modelAndView,HttpSession session) {
        if(session.getAttribute(WebSecurityConfig.SESSION_KEY) != null) {
            modelAndView.setViewName("redirect:/index");
        }
        modelAndView.setViewName("login.html");
        return modelAndView;
    }

    @RequestMapping("/toLogin")
    public ModelAndView login(Employee employee, HttpSession session,ModelAndView modelAndView){
        List<Employee> list = employeeService.checkAccount(employee.getAccount(),employee.getPassword());
        if(list == null || list.size() == 0) {
            String message = "登录失败，账号或密码错误！";
            modelAndView.addObject("message",message);
            modelAndView.setViewName("login.html");
            return modelAndView;
        }
        session.setAttribute(WebSecurityConfig.SESSION_KEY,list.get(0));
        Department department = departmentService.getDepById(list.get(0).getDeptId());
        String[] auths = department.getAuth().split(",");
        List<SideBar> sideBars = new ArrayList<>();
        List<Integer> indexs = new ArrayList<>();
        for(String auth : auths) {
            int index = Integer.valueOf(auth);
            SideBar sideBar = new SideBar();
            sideBar.setIcon(authComponent.getIcon().get(index));
            sideBar.setName(authComponent.getName().get(index));
            sideBar.setUrl(authComponent.getUrl().get(index));
            sideBars.add(sideBar);
            indexs.add(index);
        }
        session.setAttribute(WebSecurityConfig.SESSION_AUTH,sideBars);
        session.setAttribute(WebSecurityConfig.SESSION_INDEX,indexs);

        modelAndView.setViewName("index.html");
        return modelAndView;
    }

    @RequestMapping("/logout")
    public ModelAndView logout(ModelAndView modelAndView, HttpSession session) {
        // 移除session
        session.removeAttribute(WebSecurityConfig.SESSION_KEY);
        session.removeAttribute(WebSecurityConfig.SESSION_AUTH);
        session.removeAttribute(WebSecurityConfig.SESSION_INDEX);

        modelAndView.setViewName("redirect:/login");
        return modelAndView;
    }
}

