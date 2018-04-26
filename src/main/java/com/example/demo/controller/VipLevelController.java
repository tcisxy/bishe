package com.example.demo.controller;

import com.example.demo.entity.VipLevel;
import com.example.demo.service.VipLevelService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class VipLevelController {

    @Resource
    VipLevelService vipLevelService;

    @RequestMapping("/list/vipLevel")
    public String list(Model model) {
        List<VipLevel> vipLevels = vipLevelService.getVipLevelList();
        model.addAttribute("vipLevels", vipLevels);
        return "vipLevel/list.html";
    }

    @RequestMapping("/toAdd/vipLevel")
    public String toAdd() {
        return "vipLevel/addVipLevel.html";
    }

    @RequestMapping("/add/vipLevel")
    public String add(VipLevel vipLevel) {
        vipLevelService.save(vipLevel);
        return "redirect:/list/vipLevel";
    }

    @RequestMapping("/toEdit/vipLevel")
    public String toEdit(Model model, Long id) {
        VipLevel vipLevel = vipLevelService.getVipLevelById(id);
        model.addAttribute("vipLevel",vipLevel);
        return "vipLevel/editVipLevel.html";
    }

    @RequestMapping("/edit/vipLevel")
    public String edit(VipLevel vipLevel) {
        vipLevelService.edit(vipLevel);
        return "redirect:/list/vipLevel";
    }
}
