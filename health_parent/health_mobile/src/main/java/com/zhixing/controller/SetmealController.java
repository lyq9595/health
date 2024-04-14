package com.zhixing.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhixing.constant.MessageConstant;
import com.zhixing.entity.Result;
import com.zhixing.pojo.Setmeal;
import com.zhixing.service.SetmealService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*
套餐管理
* */
@RestController
@RequestMapping("setmeal")
public class SetmealController {

    @Reference
    private SetmealService setmealService;

    //查询所有套餐
    @PostMapping("/getAllSetmeal")
    public Result getAllSetmeal(){
        try {
            List<Setmeal> list=setmealService.findAll();
            return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }


    }

    //根据套餐ID查询套餐详情（套餐基本信息、套餐对应的检查组）
    @PostMapping("/findById")
    public Result findById(int id){
        try {
            Setmeal setmeal=setmealService.findById(id);
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }


    }

}


















