package com.zhixing.jobs;

import com.zhixing.constant.RedisConstant;
import com.zhixing.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * 实现定时清理垃圾图片
 *
 */
public class ClearImgJob {
    @Autowired
    private JedisPool jedisPool;

    public void clearImg(){
        //根据Redis中保存的两个set集合进行差值计算 获得垃圾图片名称集合
        Set<String> set = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        if (set !=null){
            for (String picName : set) {
                //调用七牛云清理图片 并且从七牛云删除
                QiniuUtils.deleteFileFromQiniu(picName);
                jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,picName);
                System.out.println("自定义任务执行，清理垃圾图片"+picName);
            }
        }

    }

}





























