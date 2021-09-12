package com.zjh.clouddisk.service.impl;

import com.zjh.clouddisk.dao.User;
import com.zjh.clouddisk.service.loginService;
import com.zjh.clouddisk.model.UserDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author TheZJH
 * @version 1.0
 */
@Service
public class loginServiceImpl implements loginService{
    @Resource
    private UserDao userDao;
    @Override
    public User login(String username, String password) {
        return userDao.login(username,password);
    }

    @Override
    public int register(String username, String password, String realname) {
        User user=new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRealname(realname);
        int i = userDao.insertSelective(user);
        return i;
    }

    @Override
    public List<User> search() {
        return userDao.selectAll();
    }

    @Override
    public int delete(Integer userId) {
        return userDao.deleteByPrimaryKey(userId);
    }
}
