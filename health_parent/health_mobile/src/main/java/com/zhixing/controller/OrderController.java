package com.zhixing.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.zhixing.constant.MessageConstant;
import com.zhixing.constant.RedisMessageConstant;
import com.zhixing.entity.Result;
import com.zhixing.pojo.Order;
import com.zhixing.service.OrderService;
import com.zhixing.utils.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/*
    体检预约
* */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private OrderService orderService;

    //在线体检预约
    @RequestMapping("/submit")
    public Result submit(@RequestBody Map map) {
        //先从redis中获取保存的验证码 进行比对
        String telephone = (String) map.get("telephone");
        System.out.println(telephone);
        String validateCodeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        String validateCode = (String) map.get("validateCode");
        if (validateCodeInRedis != null && validateCode != null && validateCode.equals(validateCodeInRedis)) {
            map.put("orderType", Order.ORDERTYPE_WEIXIN);//设置预约类型，分为微信预约、和电话预约
            Result result = null;
            try {
                result = orderService.order(map);//通过Dubbo远程调用服务实现在线预约业务处理
            } catch (Exception e) {
                e.printStackTrace();
                return result;
            }
            if (result.isFlag()) {
                //预约成功发送短信
                SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE, telephone, (String) map.get("orderDate"));
            }
            return result;
        } else {
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }

    }


    //根据预约id查询预约相关信息
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            Map map = orderService.findById(id);
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }

    }
}























