package com.example.demo.controller;

import com.example.demo.component.AuthComponent;
import com.example.demo.entity.Department;
import com.example.demo.param.SideBar;
import com.example.demo.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class DepartmentController {
    @Resource
    DepartmentService departmentService;
    @Autowired
    AuthComponent authComponent;

    @RequestMapping("/list/dept")
    public String list(Model model) {
        List<Department> departmentList = departmentService.getDepList();
        model.addAttribute("departments", departmentList);
        return "department/list.html";
    }

    @RequestMapping("/toAdd/dept")
    public String toAdd(Model model) {
        List<SideBar> sideBars = new ArrayList<>();
        for (int i = 0; i < authComponent.getIcon().size(); i++) {
            SideBar sideBar = new SideBar();
            sideBar.setUrl(authComponent.getIcon().get(i));
            sideBar.setName(authComponent.getName().get(i));
            sideBar.setIcon(authComponent.getIcon().get(i));
            sideBar.setShow(false);
            sideBars.add(sideBar);
        }
        model.addAttribute("auths", sideBars);
        return "department/addDept.html";
    }

    @RequestMapping("/add/dept")
    public String add(Department department) {
        departmentService.save(department);
        return "redirect:/list/dept";
    }

    @RequestMapping("/toEdit/dept")
    public String toEdit(Model model, Long id) {
        if (id == 1) {
            return "redirect:/list/dept";
        }
        Department department = departmentService.getDepById(id);
        model.addAttribute("department", department);

        String[] auths = department.getAuth().split(",");
        List<SideBar> sideBars = new ArrayList<>();
        for (int i = 0; i < authComponent.getIcon().size(); i++) {
            SideBar sideBar = new SideBar();
            sideBar.setUrl(authComponent.getIcon().get(i));
            sideBar.setName(authComponent.getName().get(i));
            sideBar.setIcon(authComponent.getIcon().get(i));
            if (Arrays.asList(auths).contains(i + "")) {
                sideBar.setShow(true);
            } else {
                sideBar.setShow(false);
            }
            sideBars.add(sideBar);
        }
        model.addAttribute("auths", sideBars);
        return "/department/editDept.html";
    }

    @RequestMapping("/edit/dept")
    public String edit(Department department) {
        departmentService.save(department);
        return "redirect:/list/dept";
    }

    @RequestMapping("/delete/dept")
    public String delete(Long id) {
        if (id == 1) {
            return "redirect:/list/dept";
        }
        departmentService.delete(id);
        return "redirect:/list/dept";
    }
}
