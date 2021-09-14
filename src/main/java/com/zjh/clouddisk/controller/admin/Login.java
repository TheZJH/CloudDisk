package com.zjh.clouddisk.controller.admin;


import com.obs.services.ObsClient;
import com.zjh.clouddisk.dao.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import com.zjh.clouddisk.service.loginService;

/**
 * @author TheZJH
 * @version 1.0
 */
@Controller
public class Login {
    @Resource
    private loginService loginService;


    @PostMapping("/error")
    public String java() {
        return "error";
    }

    @GetMapping({"/login", "", "/"})
    public String login() {
        return "login";
    }


    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        HttpSession session) {
        User login = loginService.login(username, password);
        if (username.isEmpty() || password.isEmpty()) {
            session.setAttribute("msg", "用户名或密码不能为空");
            return "login";
        }
        if (login != null) {
            //保存用户信息
            session.setAttribute("userId", login.getUserId());
            session.setAttribute("realname", login.getRealname());
            //重定向防止表单重复提交
            Integer state = login.getState();
            //判断是否是管理员
            if (state.equals(1)) {
                return "redirect:/index";
            }
            if (state.equals(0)) {
                return "redirect:/user";
            } else {
                return "redirect:/error";
            }
        } else {
            session.setAttribute("msg", "用户名或密码错误");
            return "login";
        }
    }
    @GetMapping("/index")
    public String index(){
        return "index";
    }
}
