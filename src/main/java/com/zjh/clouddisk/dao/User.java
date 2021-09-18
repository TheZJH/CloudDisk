package com.zjh.clouddisk.dao;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * user
 * @author 
 */
@Data
public class User{
    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 用户的openid
     */
    private String openId;

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

}