package com.zjh.clouddisk.controller.admin;


import com.obs.services.ObsClient;
import com.obs.services.model.BucketQuota;
import com.obs.services.model.BucketStorageInfo;
import com.zjh.clouddisk.dao.BucketVO;
import com.zjh.clouddisk.dao.CloudFile;
import com.zjh.clouddisk.dao.Folder;
import com.zjh.clouddisk.dao.User;
import com.zjh.clouddisk.mapper.BucketMapper;
import com.zjh.clouddisk.mapper.FileMapper;
import com.zjh.clouddisk.mapper.FolderMapper;
import com.zjh.clouddisk.util.CloudConfig;
import com.zjh.clouddisk.util.GetSize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import com.zjh.clouddisk.service.UserService;

import java.util.List;

/**
 * @author TheZJH
 * @version 1.0
 */
@Controller
public class LoginController {
    @Autowired
    private UserService userService;

    @Resource
    private BucketMapper bucketMapper;

    @Resource
    private FileMapper fileMapper;

    @Resource
    private FolderMapper folderMapper;

    final ObsClient obsClient = new ObsClient(CloudConfig.ak, CloudConfig.sk, CloudConfig.endPoint);

    /**
     * 跳转到登陆界面
     *
     * @return
     */
    @GetMapping("/login")
    public String login() {
        return "auth-sign-in";
    }

    /**
     * 登录界面表单验证
     *
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
        User login = userService.login(username, password);
        if (username.isEmpty() || password.isEmpty()) {
            session.setAttribute("msg", "用户名或密码不能为空");
            return "auth-sign-in";
        }
        if (login != null) {
            //保存用户信息
            session.setAttribute("loginUser", login);
            session.setAttribute("msg", null);

            Integer role = login.getRole();
            //判断是否是管理员
            if (role.equals(1)) {
                //重定向防止表单重复提交
                return "redirect:/index";
            }
            if (role.equals(0)) {
                return "redirect:/index";
            } else {
                return "redirect:/page-error";
            }
        } else {
            session.setAttribute("msg", "用户名或密码错误");
            return "auth-sign-in";
        }
    }


    @GetMapping("/index")
    public String index(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loginUser");
        Integer bucketId = user.getBucketId();
        String bucketName = bucketMapper.findBucketName(bucketId);
        BucketStorageInfo storageInfo = obsClient.getBucketStorageInfo(bucketName);
        BucketQuota quota = obsClient.getBucketQuota(bucketName);

        session.setAttribute("bucket", BucketVO.builder()
                .bucketId(bucketId)
                .bucketName(bucketName)
                .bucketObjectNumber(storageInfo.getObjectNumber())
                .bucketSize(GetSize.getSize(storageInfo.getSize()))
                .bucketQuota(quota).build());
        List<CloudFile> files = fileMapper.indexFile();
        List<Folder> folders = folderMapper.indexFolder();
        model.addAttribute("indexFile", files);
        model.addAttribute("indexFolder",folders);
        return "index";
    }

    /**
     * 去注册页面
     *
     * @return
     */
    @GetMapping("/register")
    public String toRegisterPage() {
        return "auth-sign-up";
    }

}
