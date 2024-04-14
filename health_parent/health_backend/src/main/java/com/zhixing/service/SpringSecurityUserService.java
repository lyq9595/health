package com.zhixing.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhixing.pojo.Permission;
import com.zhixing.pojo.Role;
import com.zhixing.pojo.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Component
public class SpringSecurityUserService implements UserDetailsService {

    //使用dubbo通过网络远程调用提供方获取数据库中的信息
    @Reference
    private UserService userService;

    //根据用户名查询数据库获取用户信息
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        System.out.println(user);
        if (user == null){
            return null;
        }
        List<GrantedAuthority> list=new ArrayList<>();
        //动态为当前用户授权
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            //遍历角色集合，为用户授予角色
            list.add(new SimpleGrantedAuthority(role.getKeyword()));
            Set<Permission> permissions = role.getPermissions();
            //遍历权限集合，为用户授权
            for (Permission permission : permissions) {
                list.add(new SimpleGrantedAuthority(permission.getKeyword()));
            }
        }
        org.springframework.security.core.userdetails.User securityUser=new org.springframework.security.core.userdetails.User(username,user.getPassword(),list);
        return securityUser;
    }
}



























