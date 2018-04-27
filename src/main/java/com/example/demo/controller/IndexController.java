package com.example.demo.controller;

import com.example.demo.component.AuthComponent;
import com.example.demo.component.WebSecurityConfig;
import com.example.demo.entity.Consume;
import com.example.demo.entity.Department;
import com.example.demo.entity.Employee;
import com.example.demo.param.IndexData;
import com.example.demo.param.SideBar;
import com.example.demo.service.*;
import com.example.demo.util.IndexUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class IndexController {

    @Autowired
    AuthComponent authComponent;

    @Resource
    EmployeeService employeeService;
    @Resource
    DepartmentService departmentService;
    @Resource
    UserService userService;
    @Resource
    DoctorService doctorService;
    @Resource
    AppointmentService appointmentService;
    @Resource
    VisitService visitService;
    @Resource
    ConsumeService consumeService;

    @RequestMapping("/index")
    public ModelAndView index(ModelAndView modelAndView) {
        modelAndView.addObject("user",userService.count());
        modelAndView.addObject("doctor",doctorService.count());
        modelAndView.addObject("appoint",appointmentService.count());
        modelAndView.addObject("visit",visitService.count());
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

        modelAndView.setViewName("redirect:/index");
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

    @RequestMapping("/data/con")
    public IndexData consumeData() {
        Map<String,Long> map = new HashMap<>();
        List<Consume> consumes = consumeService.getConsumeListOrderByTime();
        for(Consume consume : consumes) {
            String time = new SimpleDateFormat("yyyy-MM-dd").format(consume.getTime());
            if(map.get(time) == null) {
                map.put(time,consume.getMoney());
            }else {
                map.put(time,map.get(time) + consume.getMoney());
            }
        }
        return IndexUtil.getIndexData(map);
    }
}

