package com.zhixing.dao;

import com.zhixing.pojo.User;
import org.apache.ibatis.annotations.Select;

public interface UserDao {

    //根据用户名查询数据库获取用户信息和角色关联信息，同时查询角色关联的权限信息
    @Select("select * from t_user where username=#{username}")
    public User findByUsername(String username);




}



















