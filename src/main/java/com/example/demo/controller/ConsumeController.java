package com.example.demo.controller;

import com.example.demo.component.PayComponent;
import com.example.demo.entity.Consume;
import com.example.demo.entity.User;
import com.example.demo.param.QueryParam;
import com.example.demo.service.ConsumeService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class ConsumeController {
    @Resource
    ConsumeService consumeService;
    @Resource
    UserService userService;
    @Autowired
    PayComponent payComponent;

    @RequestMapping("/list/consume")
    public ModelAndView list(ModelAndView modelAndView) {
        List<Consume> consumes = consumeService.getConsumeList();
        modelAndView.addObject("consumes", consumes);
        modelAndView.setViewName("consume/list.html");
        return modelAndView;
    }

    @RequestMapping("/toAdd/consume")
    public ModelAndView toAdd(ModelAndView modelAndView) {
        modelAndView.setViewName("consume/addConsume.html");
        modelAndView.addObject("pays",payComponent.getType());
        return modelAndView;
    }

    @RequestMapping("/add/consume")
    public ModelAndView add(ModelAndView modelAndView, Consume consume) {
        User _user = userService.getUserByPhone(consume.getPhone());
        if(_user == null) {
            String message = "此用户不存在！";
            modelAndView.addObject("message",message);
            return toAdd(modelAndView);
        }
        consumeService.save(consume);
        modelAndView.setViewName("redirect:/list/consume");
        return modelAndView;
    }

    @RequestMapping("/toEdit/consume")
    public ModelAndView toEdit(ModelAndView modelAndView, Long id) {
        Consume consume = consumeService.findConsumeById(id);
        modelAndView.addObject("consume",consume);
        modelAndView.addObject("pays",payComponent.getType());
        modelAndView.setViewName("consume/editConsume.html");
        return modelAndView;
    }

    @RequestMapping("/edit/consume")
    public ModelAndView edit(ModelAndView modelAndView, Consume consume) {
        consumeService.edit(consume);
        modelAndView.setViewName("redirect:/list/consume");
        return modelAndView;
    }

    @RequestMapping("/query/consume")
    public ModelAndView query(ModelAndView modelAndView, QueryParam queryParam) {
        List<Consume> consumes = consumeService.getConsumeListByParam(queryParam);
        modelAndView.addObject("phone", queryParam.getPhone());
        modelAndView.addObject("consumes",consumes);
        modelAndView.setViewName("consume/list.html");
        return modelAndView;
    }

}
