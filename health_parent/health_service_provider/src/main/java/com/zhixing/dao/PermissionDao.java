package com.zhixing.dao;

import com.zhixing.pojo.Permission;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

public interface PermissionDao {

    //根据角色id查询关联权限
    @Select("select p.*  " +
            "from t_permission p, t_role_permission rp " +
            "where p.id=rp.permission_id and rp.role_id=#{roleId}")
    public Set<Permission> findByRoleId(Integer roleId);








}













