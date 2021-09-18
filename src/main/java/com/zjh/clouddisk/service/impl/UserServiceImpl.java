package com.zjh.clouddisk.service.impl;

import com.zjh.clouddisk.dao.User;
import com.zjh.clouddisk.mapper.UserMapper;
import com.zjh.clouddisk.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author TheZJH
 * @version 1.0
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public User login(String username, String password) {
        return userMapper.login(username,password);
    }
}
