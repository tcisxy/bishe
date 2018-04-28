package com.example.demo.controller;

import com.example.demo.component.PayComponent;
import com.example.demo.entity.Consume;
import com.example.demo.entity.Doctor;
import com.example.demo.entity.User;
import com.example.demo.entity.Visit;
import com.example.demo.param.QueryParam;
import com.example.demo.repository.ConsumeRepository;
import com.example.demo.service.ConsumeService;
import com.example.demo.service.DoctorService;
import com.example.demo.service.UserService;
import com.example.demo.service.VisitService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class VisitController {
    @Resource
    VisitService visitService;
    @Resource
    DoctorService doctorService;
    @Resource
    UserService userService;
    @Resource
    ConsumeService consumeService;
    @Autowired
    PayComponent payComponent;

    @RequestMapping("/list/visit")
    public ModelAndView list(ModelAndView modelAndView) {
        List<Doctor> doctors = doctorService.getDoctorList();
        modelAndView.addObject("doctors",doctors);
        List<Visit> visits = visitService.getUserVisitList();
        modelAndView.addObject("visits", visits);
        modelAndView.setViewName("visit/list.html");
        return modelAndView;
    }

    @RequestMapping("/toAdd/visit")
    public ModelAndView toAdd(ModelAndView modelAndView) {
        List<Doctor> doctors = doctorService.getDoctorList();
        modelAndView.addObject("doctors", doctors);
        modelAndView.setViewName("/visit/addVisit.html");
        return modelAndView;
    }

    @RequestMapping("/add/visit")
    public ModelAndView add(ModelAndView modelAndView, Visit visit) {
        User user = userService.getUserByPhone(visit.getPhone());
        if(user == null) {
            String message = "此用户不存在！";
            modelAndView.addObject("message",message);
            modelAndView.setViewName("visit/list.html");
        }
        visitService.save(visit);
        modelAndView.setViewName("redirect:/list/visit");
        return modelAndView;
    }

    @RequestMapping("/toEdit/visit")
    public ModelAndView toEdit(ModelAndView modelAndView, Long id) {
        Visit visit = visitService.findById(id);
        modelAndView.addObject("visit",visit);
        modelAndView.setViewName("visit/editVisit.html");
        return modelAndView;
    }

    @RequestMapping("/edit/visit")
    public ModelAndView edit(ModelAndView modelAndView, Visit visit) {
        visitService.edit(visit);
        modelAndView.setViewName("redirect:/list/visit");
        return modelAndView;
    }

    @RequestMapping("/toDeal/visit")
    public ModelAndView toDeal(ModelAndView modelAndView, Long id) {
        Visit visit = visitService.findById(id);
        modelAndView.addObject("visit",visit);
        modelAndView.addObject("pays",payComponent.getType());
        modelAndView.setViewName("visit/dealVisit.html");
        return modelAndView;
    }

    @RequestMapping("/deal/visit")
    public ModelAndView deal(ModelAndView modelAndView, Consume consume) {
        Consume newConsume = new Consume();
        Visit visit = visitService.findById(consume.getId());
        newConsume.setType(consume.getType());
        newConsume.setMoney(consume.getMoney());
        newConsume.setUserId(visit.getUserId());
        newConsume.setConsumeStatus(0);
        visit.setStatus(1);
        visit.setConsumeId(consumeService.save(newConsume).getId());
        visitService.save(visit);
        modelAndView.setViewName("redirect:/list/visit");
        return modelAndView;
    }

    @RequestMapping("/query/visit")
    public ModelAndView query(ModelAndView modelAndView, QueryParam queryParam) {
        List<Doctor> doctors = doctorService.getDoctorList();
        modelAndView.addObject("doctors",doctors);
        modelAndView.addObject("visits",visitService.queryByParam(queryParam));
        modelAndView.setViewName("visit/list.html");
        modelAndView.addObject("phone",queryParam.getPhone());
        modelAndView.addObject("name", queryParam.getName());
        modelAndView.addObject("startTime", queryParam.getStartTime());
        modelAndView.addObject("endTime", queryParam.getEndTime());
        modelAndView.addObject("doctorId", queryParam.getDoctorId());
        return modelAndView;
    }
}
