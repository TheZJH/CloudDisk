package com.zjh.clouddisk.controller.admin;


import com.zjh.clouddisk.dao.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import com.zjh.clouddisk.service.UserService;

/**
 * @author TheZJH
 * @version 1.0
 */
@Controller
public class Login {
    @Resource
    private UserService userService;

    /**
     * 跳转到登陆界面
     * @return
     */
    @GetMapping("/login")
    public String login() {
        return "auth-sign-in";
    }

    /**
     * 登录界面表单验证
     * @param username
     * @param password
     * @param session
     * @return
     */
    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        HttpSession session) {
        //根据用户名密码查询
        User login = userService.login(username,password);
        if (username.isEmpty() || password.isEmpty()) {
            session.setAttribute("msg", "用户名或密码不能为空");
            return "login";
        }
        if (login != null) {
            //保存用户信息
            session.setAttribute("loginUser", login);
            session.setAttribute("msg", null);
            //重定向防止表单重复提交
            Integer role = login.getRole();
            //判断是否是管理员
            if (role.equals(1)) {
                return "redirect:/index";
            }
            if (role.equals(0)) {
                return "redirect:/index";
            } else {
                return "redirect:/error";
            }
        } else {
            session.setAttribute("msg", "用户名或密码错误");
            return "auth-sign-in";
        }
    }


    @GetMapping("/index")
    public String index() {
        return "index";
    }
}
