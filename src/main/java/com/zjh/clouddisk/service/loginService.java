package com.zjh.clouddisk.service;

import com.zjh.clouddisk.dao.User;

import java.util.List;

/**
 * @author TheZJH
 * @version 1.0
 */
public interface loginService {
    User login(String username, String password);

    int register(String username, String password, String realname);

    List<User> search();

    int delete(Integer userId);
}
