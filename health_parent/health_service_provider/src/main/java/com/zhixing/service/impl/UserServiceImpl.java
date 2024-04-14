package com.zhixing.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zhixing.dao.PermissionDao;
import com.zhixing.dao.RoleDao;
import com.zhixing.dao.UserDao;
import com.zhixing.pojo.Permission;
import com.zhixing.pojo.Role;
import com.zhixing.pojo.User;
import com.zhixing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PermissionDao permissionDao;

    //根据用户名查询数据库获取用户信息和角色关联信息，同时查询角色关联的权限信息
    @Override
    public User findByUsername(String username) {
        User user = userDao.findByUsername(username);//查询用户基本信息，不包含用户角色

        if (user ==null){
            return null;
        }
        Integer userId = user.getId();
        //根据用户id查询对应的角色
        Set<Role> roles = roleDao.findByUserId(userId);
        for (Role role : roles) {
            Integer roleId = role.getId();
            //根据角色id查询关联权限
            Set<Permission> permissions = permissionDao.findByRoleId(roleId);
            role.setPermissions(permissions);
        }
        user.setRoles(roles);//让用户关联角色
        return user;
    }
}

















