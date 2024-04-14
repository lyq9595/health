package com.zhixing.controller;

import com.zhixing.constant.MessageConstant;
import com.zhixing.entity.Result;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//用户相关操作
@RestController
@RequestMapping("/user")
public class UserController {

    //获得当前登录的用户的用户名
    @RequestMapping("/getUsername")
    public Result getUsername(){
        //当我们springSecurity完成认证后 会将当前用户信息保存到框架上下文对象中
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(user);

        if (user!=null){
            String username = user.getUsername();
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,username);
        }
        return new Result(false, MessageConstant.GET_USERNAME_FAIL);
    }

}















