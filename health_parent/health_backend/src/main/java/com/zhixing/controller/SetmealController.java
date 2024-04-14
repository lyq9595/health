package com.zhixing.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhixing.constant.MessageConstant;
import com.zhixing.constant.RedisConstant;
import com.zhixing.entity.PageResult;
import com.zhixing.entity.QueryPageBean;
import com.zhixing.entity.Result;
import com.zhixing.pojo.Setmeal;
import com.zhixing.service.SetmealService;
import com.zhixing.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.UUID;

/**
 * 体检套餐管理
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private JedisPool jedisPool;


    //文件上传
    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile") MultipartFile imgFile){
        String originalFilename = imgFile.getOriginalFilename();//原始文件名字
        int index = originalFilename.lastIndexOf(".");
        String extension = originalFilename.substring(index-1);//.jpg
        String fileName= UUID.randomUUID().toString()+extension;

        try {
            //七牛云上传
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),fileName);
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
        return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,fileName);
    }

    @Reference
    private SetmealService setmealService;

    //新增套餐
    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal,Integer[] checkgroupIds){
        try {
            setmealService.add(setmeal,checkgroupIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
        return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
    }



    //分页查询
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){

        return setmealService.pageQuery(queryPageBean);
    }
}



















