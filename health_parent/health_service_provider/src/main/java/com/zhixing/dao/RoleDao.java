package com.zhixing.dao;

import com.zhixing.pojo.Role;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

public interface RoleDao {


    //根据用户id查询关联的角色
    @Select(" select r.* " +
            " from t_role r, t_user_role ur " +
            " where r.id=ur.role_id and ur.user_id=#{userId}")
    public Set<Role> findByUserId(Integer userId);
}
















