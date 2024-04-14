package com.zhixing.service;

import com.zhixing.entity.Result;

import java.util.Map;

public interface OrderService {

    //预约提交
    public Result order(Map map) throws Exception;

    //根据预约id查询预约相关信息
    Map findById(Integer id) throws Exception;
}
























