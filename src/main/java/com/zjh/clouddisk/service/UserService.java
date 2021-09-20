package com.zjh.clouddisk.service;


import com.zjh.clouddisk.dao.User;

import java.util.List;

/**
 * @author TheZJH
 * @version 1.0
 */
public interface UserService {
    User login(String username, String password);

    int addUser(User user);

    List<User> findAll();

    int deleteUser(Integer userId);
}
