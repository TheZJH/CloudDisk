package com.zjh.clouddisk.dao;

import java.io.Serializable;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

/**
 * user
 *
 * @author
 */
@Data
@Builder
public class User implements Serializable{
    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 用户的openid
     */
    private String realName;

    /**
     * 文件仓库ID
     */
    private Integer bucketId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 密码
     */
    private String password;

    /**
     * 注册时间
     */
    private Date registerTime;

    /**
     * 头像地址
     */
    private String imagePath;

    /**
     * 用户角色,0管理员，1普通用户
     */
    private Integer role;

    private Integer phone;

    public User() {
    }

    public User(Integer userId, String realName, Integer bucketId, String username, String email, String password, Date registerTime, String imagePath, Integer role, Integer phone) {
        this.userId = userId;
        this.realName = realName;
        this.bucketId = bucketId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.registerTime = registerTime;
        this.imagePath = imagePath;
        this.role = role;
        this.phone = phone;
    }
}