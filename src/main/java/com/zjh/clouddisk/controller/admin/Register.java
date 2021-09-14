package com.zjh.clouddisk.controller.admin;

import com.zjh.clouddisk.dao.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.zjh.clouddisk.service.loginService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author TheZJH
 * @version 1.0
 */
@Controller
public class Register {
    @Resource
    loginService loginService;

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String registerPo(@RequestParam("username") String username,
                             @RequestParam("password") String password,
                             @RequestParam("realname") String realname) {
        int register = loginService.register(username, password, realname);
        if (register > 0) {
            return "test";
        }
        return "register";
    }
    @GetMapping("/user")
    public String userlist(Model model){
        List<User> users=loginService.search();
        model.addAttribute("userlist",users);
        return "user";
    }
    @GetMapping("/delete")
        public String delete(Integer userId){
        if(loginService.delete(userId)>0){
            return "test";
        }
        return "error";
    }
}
