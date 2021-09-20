package com.zjh.clouddisk.mapper;

import com.zjh.clouddisk.dao.User;
import org.apache.ibatis.annotations.*;

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
            @Result(column = "real_name", property = "realName"),
            @Result(column = "bucket_id", property = "bucketId"),
            @Result(column = "user_name", property = "username"),
            @Result(column = "email", property = "email"),
            @Result(column = "password", property = "password"),
            @Result(column = "register_time", property = "registerTime"),
            @Result(column = "image_path", property = "imagePath"),
            @Result(column = "role", property = "role"),
            @Result(column = "phone", property = "phone")
    })
    User login(String username, String password);

    /**
     * 查询所有用户
     *
     * @return
     */
    @Select("select * from user")
    @ResultMap("userMap")
    List<User> findAll();

    @Insert("insert into user(real_name,bucket_id,user_name,email,password,register_time,role,phone) values(#{realName},#{bucketId},#{username},#{email},#{password},#{registerTime},#{role},#{phone})")
    int addUser(User user);

    @Delete("delete from user where user_id=#{userId}")
    int deleteUser(Integer userId);
}
