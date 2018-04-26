package com.example.demo.controller;

import com.example.demo.component.PayComponent;
import com.example.demo.entity.Recharge;
import com.example.demo.entity.User;
import com.example.demo.service.RechargeService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class RechargeController {

    @Autowired
    PayComponent payComponent;

    @Resource
    RechargeService rechargeService;
    @Resource
    UserService userService;

    @RequestMapping("/list/recharge")
    public ModelAndView list(ModelAndView modelAndView) {
        List<Recharge> recharges = rechargeService.getRechargeList();
        modelAndView.addObject("recharges", recharges);
        modelAndView.setViewName("recharge/list.html");
        return modelAndView;
    }

    @RequestMapping("/toAdd/recharge")
    public ModelAndView toAdd(ModelAndView modelAndView) {
        modelAndView.addObject("pays",payComponent.getType());
        modelAndView.setViewName("recharge/addRecharge.html");
        return modelAndView;
    }

    @RequestMapping("/add/recharge")
    public ModelAndView add(ModelAndView modelAndView, Recharge recharge) {
        User _user = userService.getUserByPhone(recharge.getPhone());
        if(_user == null) {
            String message = "此用户不存在！";
            modelAndView.addObject("message",message);
            return toAdd(modelAndView);
        }
        rechargeService.save(recharge);
        modelAndView.setViewName("redirect:/list/recharge");
        return modelAndView;
    }

    @RequestMapping("/toEdit/recharge")
    public ModelAndView toEdit(ModelAndView modelAndView, Long id) {
        Recharge recharge = rechargeService.findRechargeById(id);
        modelAndView.addObject("recharge",recharge);
        modelAndView.addObject("pays",payComponent.getType());
        modelAndView.setViewName("recharge/editRecharge.html");
        return modelAndView;
    }

    @RequestMapping("/query/recharge")
    public ModelAndView query(ModelAndView modelAndView, Recharge recharge) {
        List<Recharge> recharges = rechargeService.getRechargeListByParam(recharge);
        modelAndView.addObject("recharges",recharges);
        modelAndView.setViewName("recharge/list.html");
        return modelAndView;
    }

    @RequestMapping("/edit/recharge")
    public ModelAndView edit(ModelAndView modelAndView, Recharge recharge) {
        rechargeService.edit(recharge);
        modelAndView.setViewName("redirect:/list/recharge");
        return modelAndView;
    }
}
