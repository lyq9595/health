package com.zhixing.controller;

import com.zhixing.constant.MessageConstant;
import com.zhixing.constant.RedisMessageConstant;
import com.zhixing.entity.Result;
import com.zhixing.utils.SMSUtils;
import com.zhixing.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping("/validateCode")

public class ValidateCodeController {

    @Autowired
    private JedisPool jedisPool;

    //用户体检预约发送验证码
    @RequestMapping("/send4Order")
    public Result send4Order(String telephone){
        //随机生成4位数字验证码
        Integer validateCode = ValidateCodeUtils.generateValidateCode(4);
        //调用阿里云短信服务
        boolean flag = SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, validateCode.toString());
        if (flag){
            jedisPool.getResource().setex(telephone+ RedisMessageConstant.SENDTYPE_ORDER,300,validateCode.toString());
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        }else {
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }

    }



    //用户手机快速登录登录
    @PostMapping("/send4Login")
    public Result send4Login(String telephone){
        //随机生成6位数字验证码
        Integer validateCode = ValidateCodeUtils.generateValidateCode(6);
        //调用阿里云短信服务
        boolean flag = SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, validateCode.toString());
        if (flag){
            jedisPool.getResource().setex(telephone+ RedisMessageConstant.SENDTYPE_LOGIN,300,validateCode.toString());
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        }else {
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }

    }


}





















