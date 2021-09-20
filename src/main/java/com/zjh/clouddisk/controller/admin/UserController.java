package com.zjh.clouddisk.controller.admin;

import com.zjh.clouddisk.dao.User;
import com.zjh.clouddisk.mapper.BucketMapper;
import com.zjh.clouddisk.mapper.UserMapper;
import com.zjh.clouddisk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * @author TheZJH
 * @version 1.0
 */
@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Resource
    private BucketMapper bucketMapper;
    Date date = new Date();

    @GetMapping("/user/add")
    public String toAddUserPage() {
        return "user-add";
    }

    /**
     * 添加用户
     *
     * @param realName
     * @param bucketName
     * @param phone
     * @param email
     * @param role
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/user/add")
    public String addUser(String realName, String bucketName, Integer phone, String email, Integer role,
                          String username, String password) {
        Integer bucketId = bucketMapper.bucketId(bucketName);

        userService.addUser(User.builder()
                .realName(realName)
                .bucketId(bucketId)
                .phone(phone)
                .email(email).role(role).username(username).password(password).registerTime(date).build());
        return "redirect:/user/add";
    }

    @GetMapping("/user/list")
    public String userList(Model model) {
        List<User> userList = userService.findAll();
        model.addAttribute("userList", userList);
        return "user-list";
    }

    @GetMapping("/user/delete")
    public String userDelete(Integer userId) {
        userService.deleteUser(userId);
        return "redirect:/user/list";
    }

    @GetMapping("test")
    public String test() {
        return "page-android";
    }

    @GetMapping("/user/profile/edit")
    public String userProfileEdit(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loginUser");
        Integer userId = user.getUserId();
        return "user-profile-edit";
    }
}
