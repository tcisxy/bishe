package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.param.QueryParam;
import com.example.demo.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    @Resource
    UserService userService;

    @RequestMapping("/list/user")
    public ModelAndView list(ModelAndView modelAndView) {
        List<User> users = userService.getUserList();
        modelAndView.addObject("users",users);
        modelAndView.setViewName("user/list.html");
        return modelAndView;
    }

    @RequestMapping("/toAdd/user")
    public ModelAndView toAdd(ModelAndView modelAndView) {
        User user = new User();
        user.setSex(1);
        modelAndView.addObject("old",user);
        modelAndView.setViewName("user/addUser.html");
        return modelAndView;
    }

    @RequestMapping("/add/user")
    public ModelAndView addUser(ModelAndView modelAndView, User user) {
        modelAndView.addObject("old",user);
        if(!user.getPhone().matches("^1[3|4|5|7|8][0-9]\\d{8}$")) {
            String message = "手机号不合法！";
            return throwError(modelAndView,message,"user/addUser.html");
        }
        User _user = userService.getUserByPhone(user.getPhone());
        if(_user != null) {
            String message = "此用户已存在，手机号不能重复！";
            return throwError(modelAndView,message,"user/addUser.html");
        }
        userService.save(user);
        modelAndView.setViewName("redirect:/list/user");
        return modelAndView;
    }

    @RequestMapping("/query/user")
    public ModelAndView queryUser(QueryParam queryParam, ModelAndView modelAndView) {
        List<User> list = userService.getUserByParam(queryParam);
        modelAndView.addObject("users",list);
        modelAndView.addObject("phone",queryParam.getPhone());
        modelAndView.addObject("name", queryParam.getName());
        modelAndView.setViewName("user/list.html");
        return modelAndView;
    }

    @RequestMapping("/toEdit/user")
    public ModelAndView toEdit(ModelAndView modelAndView,Long id){
        User user = userService.getUserById(id);
        modelAndView.addObject("user",user);
        modelAndView.setViewName("user/editUser.html");
        return modelAndView;
    }

    @RequestMapping("/edit/user")
    public ModelAndView edit(User user, ModelAndView modelAndView) {
        if(!user.getPhone().matches("^1[3|4|5|7|8][0-9]\\d{8}$")) {
            String message = "手机号不合法！";
            modelAndView.addObject("message",message);
            return toEdit(modelAndView,user.getId());
        }
        User _user = userService.getUserByPhone(user.getPhone());
        if(_user != null && _user.getId() != user.getId()) {
            String message = "此用户已存在，手机号不能重复！";
            modelAndView.addObject("message",message);
            return toEdit(modelAndView,user.getId());
        }
        userService.edit(user);
        modelAndView.setViewName("redirect:/list/user");
        return modelAndView;
    }

    @RequestMapping("/delete/user")
    public ModelAndView delete(long id, ModelAndView modelAndView) {
        userService.delete(id);
        modelAndView.setViewName("redirect:/list/user");
        return modelAndView;
    }

    @RequestMapping("/check/user")
    public ModelAndView rechargeCheck(ModelAndView modelAndView) {
        userService.checkUserMoney();
        modelAndView.setViewName("redirect:/list/user");
        return modelAndView;
    }

    public static ModelAndView throwError(ModelAndView modelAndView, String message, String view) {
        modelAndView.setViewName(view);
        modelAndView.addObject("message",message);
        return modelAndView;
    }
}
