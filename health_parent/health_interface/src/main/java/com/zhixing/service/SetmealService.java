package com.zhixing.service;

import com.zhixing.entity.PageResult;
import com.zhixing.entity.QueryPageBean;
import com.zhixing.entity.Result;
import com.zhixing.pojo.Setmeal;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

public interface SetmealService {

    //新增套餐
    public void add(Setmeal setmeal, Integer[] checkgroupIds);

    //分页查询
    PageResult pageQuery(QueryPageBean queryPageBean);

    //查询所有套餐
    List<Setmeal> findAll();

    //根据套餐ID查询套餐详情（套餐基本信息、套餐对应的检查组）
    Setmeal findById(int id);

    //查询套餐及人数
    List<Map<String, Object>> findSetmealCount();

}


























