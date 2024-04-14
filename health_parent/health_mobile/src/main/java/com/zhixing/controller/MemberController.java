package com.zhixing.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.qiniu.util.Json;
import com.zhixing.constant.MessageConstant;
import com.zhixing.constant.RedisMessageConstant;
import com.zhixing.entity.Result;
import com.zhixing.pojo.Member;
import com.zhixing.pojo.Order;
import com.zhixing.service.MemberService;
import com.zhixing.utils.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * 处理会员相关操作
 */

@RestController
@RequestMapping("/member")
public class MemberController {
    @Autowired
    private JedisPool jedisPool;

    @Reference
    private MemberService memberService;

    //手机号快速登陆
    @RequestMapping("/check")
    public Result login(@RequestBody Map map, HttpServletResponse response){
        //从redis中获取保存的验证码
        String telephone = (String) map.get("telephone");
        String validateCode = (String) map.get("validateCode");
        String validateCodeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        if (validateCodeInRedis != null && validateCode != null && validateCode.equals(validateCodeInRedis)) {
           //判断当前用户是否为会员（注册）
            Member member = memberService.findByTelephone(telephone);
            if (member==null){
                //不是会员 自动完成注册
                member=new Member();
                member.setPhoneNumber(telephone);
                member.setRegTime(new Date());
                memberService.add(member);
            }
            //向客户端写入cookies，内容为手机号
            Cookie cookie=new Cookie("login_member_telephone",telephone);
            cookie.setPath("/");//路径
            cookie.setMaxAge(60*60*24*30);
            response.addCookie(cookie);
            //将会员信息保存到redis
            String json = JSON.toJSON(member).toString();
            jedisPool.getResource().setex(telephone,60*30,json);
            return new Result(true,MessageConstant.LOGIN_SUCCESS);
        } else {
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }

    }




}






























