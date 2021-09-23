package com.zjh.clouddisk.controller.admin;

import com.zjh.clouddisk.dao.User;
import com.zjh.clouddisk.mapper.FileRootMapper;
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
import java.util.UUID;

/**
 * @author TheZJH
 * @version 1.0
 */
@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Resource
    private FileRootMapper fileRootMapper;

    @GetMapping("/user/add")
    public String toAddUserPage() {
        return "user-add";
    }

    /**
     * 添加用户
     *
     * @param realName
     * @param
     * @param phone
     * @param email
     * @param role
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/user/add")
    public String addUser(String realName, @RequestParam("bucketName") Integer bucketId, Integer phone, String email, Integer role,
                          String username, String password, Integer folderId) {
        //获取当前时间
        Date date = new Date();
        userService.addUser(User.builder()
                .realName(realName)
                .bucketId(1)
                .phone(phone)
                .folderId(folderId)
                .bucketId(bucketId)
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

    @GetMapping("/user/profile/edit")
    public String userProfileEdit(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loginUser");
        Integer userId = user.getUserId();
        return "user-profile-edit";
    }

    @GetMapping("/user/update")
    public String userFileEdit(HttpSession session, Integer userId) {
        User userById = userService.findUserById(userId);
        session.setAttribute("user", userById);
        return "user-profile";
    }

    @PostMapping("/user/change")
    public String userUpdate(String realName, Integer phone, String email,
                             String username, String password, Integer userId) {
        fileRootMapper.updateUser(realName, username, email, password, phone
                , userId);
        return "redirect:/user-profile-edit";
    }

    @GetMapping("/group/list")
    public String toBucketPage() {
        return "group-list";
    }

    @PostMapping("/group/add")
    public String addGroup(Integer folderId, String realName, HttpSession session) {
        User user = (User) session.getAttribute("loginUser");
        if (user.getRole() == 0) {
            fileRootMapper.insertFolderUser(folderId, fileRootMapper.getUserId(realName));
            return "redirect:/file?folderId=" + folderId;
        } else {
            session.setAttribute("msg", "你没有权限添加群组成员别乱点了");
            return "pages-error";
        }
    }
}
