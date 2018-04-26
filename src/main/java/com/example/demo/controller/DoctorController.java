package com.example.demo.controller;

import com.example.demo.entity.Doctor;
import com.example.demo.service.DoctorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class DoctorController {
    @Resource
    DoctorService doctorService;

    @RequestMapping("/list/doctor")
    public ModelAndView list(ModelAndView modelAndView) {
        List<Doctor> doctors = doctorService.getDoctorList();
        modelAndView.addObject("doctors",doctors);
        modelAndView.setViewName("doctor/list.html");
        return modelAndView;
    }

    @RequestMapping("/toAdd/doctor")
    public ModelAndView toAdd(ModelAndView modelAndView) {
        modelAndView.setViewName("doctor/addDoctor.html");
        return modelAndView;
    }

    @RequestMapping("/add/doctor")
    public ModelAndView add(ModelAndView modelAndView, Doctor doctor) {
        if(!StringUtils.isEmpty(doctor.getPhone()) && !doctor.getPhone().matches("^1[3|4|5|7|8][0-9]\\d{8}$")) {
            String message = "手机号不合法！";
            UserController.throwError(modelAndView,message,"doctor/addDoctor.html");
        }
        doctorService.save(doctor);
        modelAndView.setViewName("redirect:/list/doctor");
        return modelAndView;
    }

    @RequestMapping("/toEdit/doctor")
    public ModelAndView toEdit(ModelAndView modelAndView, Long id) {
        Doctor doctor = doctorService.getDoctorById(id);
        modelAndView.addObject("doctor",doctor);
        modelAndView.setViewName("doctor/editDoctor.html");
        return modelAndView;
    }

    @RequestMapping("/edit/doctor")
    public ModelAndView edit(ModelAndView modelAndView, Doctor doctor) {
        if(!StringUtils.isEmpty(doctor.getPhone()) && !doctor.getPhone().matches("^1[3|4|5|7|8][0-9]\\d{8}$")) {
            String message = "手机号不合法！";
            modelAndView.addObject("message",message);
            return toEdit(modelAndView,doctor.getId());
        }
        doctorService.edit(doctor);
        modelAndView.setViewName("redirect:/list/doctor");
        return modelAndView;
    }

    @RequestMapping("/delete/doctor")
    public ModelAndView delete(ModelAndView modelAndView, Long id) {
        doctorService.delete(id);
        modelAndView.setViewName("redirect:/list/doctor");
        return modelAndView;
    }

    @RequestMapping("/check/doctor")
    public ModelAndView check(ModelAndView modelAndView) {
        doctorService.checkDoctorInfo();
        modelAndView.setViewName("redirect:/list/doctor");
        return modelAndView;
    }


}
