package com.zjh.clouddisk.mapper;

import com.zjh.clouddisk.dao.User;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author TheZJH
 * @version 1.0
 */
public interface UserMapper {
    /**
     * 登录
     *
     * @param username
     * @param password
     * @return
     */
    @Select("select * from user where user_name=#{username} and password=#{password}")
    @Results(id = "userMap", value = {
            @Result(id = true, column = "user_id", property = "userId"),
            @Result(column = "open_id", property = "openId"),
            @Result(column = "bucket_id", property = "bucketId"),
            @Result(column = "user_name", property = "username"),
            @Result(column = "email", property = "email"),
            @Result(column = "password", property = "password"),
            @Result(column = "register_time", property = "registerTime"),
            @Result(column = "image_path", property = "imagePath"),
            @Result(column = "role", property = "role"),
    })
    User login(String username, String password);

    /**
     * 查询所有用户
     * @return
     */
    @Select("select * from user")
    @ResultMap("userMap")
    List<User> findAll();
}
