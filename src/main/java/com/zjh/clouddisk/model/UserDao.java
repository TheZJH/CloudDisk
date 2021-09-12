package com.zjh.clouddisk.model;

import com.zjh.clouddisk.dao.User;

import java.util.List;

public interface UserDao {
    int deleteByPrimaryKey(Integer userId);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer userId);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User login(String username,String password);

    List<User> selectAll();
}