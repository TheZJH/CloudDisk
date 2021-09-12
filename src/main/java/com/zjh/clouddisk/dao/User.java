package com.zjh.clouddisk.dao;

import java.io.Serializable;
import lombok.Data;

/**
 * user
 * @author 
 */
@Data
public class User implements Serializable {
    /**
     * 编号
     */
    private Integer userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 真实姓名
     */
    private String realname;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String mall;

    /**
     * 状态
     */
    private Integer state;

    /**
     * 职位
     */
    private String postion;

    /**
     * 创建时间
     */
    private String created;

    private static final long serialVersionUID = 1L;
}