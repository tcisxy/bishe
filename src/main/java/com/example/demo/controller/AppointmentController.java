package com.example.demo.controller;

import com.example.demo.entity.Appointment;
import com.example.demo.entity.Doctor;
import com.example.demo.entity.User;
import com.example.demo.entity.Visit;
import com.example.demo.service.AppointmentService;
import com.example.demo.service.DoctorService;
import com.example.demo.service.UserService;
import com.example.demo.service.VisitService;
import com.fasterxml.jackson.datatype.jsr310.ser.YearSerializer;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;

@RestController
public class AppointmentController {
    @Resource
    DoctorService doctorService;
    @Resource
    AppointmentService appointmentService;
    @Resource
    UserService userService;
    @Resource
    VisitService visitService;

    @RequestMapping("/list/appoint")
    public ModelAndView list(ModelAndView modelAndView) {
        List<Appointment> appointments = appointmentService.getAppointList();
        modelAndView.addObject("appointments", appointments);
        modelAndView.setViewName("appoint/list.html");
        return modelAndView;
    }

    @RequestMapping("/toAdd/appoint")
    public ModelAndView toAdd(ModelAndView modelAndView) {
        List<Doctor> doctors = doctorService.getDoctorList();
        modelAndView.addObject("doctors",doctors);
        modelAndView.setViewName("appoint/addAppoint.html");
        return modelAndView;
    }

    @RequestMapping("/add/appoint")
    public ModelAndView add(ModelAndView modelAndView, Appointment appointment) {
        User _user = userService.getUserByPhone(appointment.getPhone());
        if(_user == null) {
            String message = "此用户不存在！";
            modelAndView.addObject("message",message);
            return toAdd(modelAndView);
        }
        appointmentService.save(appointment);
        modelAndView.setViewName("redirect:/list/appoint");
        return modelAndView;
    }

    @RequestMapping("/toEdit/appoint")
    public ModelAndView toEdit(ModelAndView modelAndView, Long id) {
        Appointment appointment = appointmentService.getAppointmentById(id);
        modelAndView.addObject("appointment",appointment);
        modelAndView.setViewName("appoint/editAppoint.html");
        return modelAndView;
    }

    @RequestMapping("/edit/appoint")
    public ModelAndView edit(Appointment appointment, ModelAndView modelAndView) {
        appointmentService.edit(appointment);
        modelAndView.setViewName("redirect:/list/appoint");
        return modelAndView;
    }

    @RequestMapping("/query/appoint")
    public ModelAndView query(Appointment appointment,ModelAndView modelAndView) {
        List<Appointment> appointments = appointmentService.getAppointmentByPhone(appointment.getPhone());
        modelAndView.addObject("appointments",appointments);
        modelAndView.setViewName("appoint/list.html");
        return modelAndView;
    }

    @RequestMapping("/toQueue/appoint")
    @Transactional
    public ModelAndView queue(ModelAndView modelAndView, long id) {
        Appointment appointment = appointmentService.getAppointmentById(id);
        modelAndView.setViewName("redirect:/list/appoint");
        if(appointment.getTime().after(new Timestamp(System.currentTimeMillis()))) {
            return modelAndView;
        }
        appointment.setAppointStatus(1);
        appointmentService.save(appointment);
        Visit visit = new Visit();
        visit.setDoctorId(appointment.getDoctorId());
        visit.setPhone(appointment.getPhone());
        visitService.save(visit);

        return modelAndView;
    }
}
