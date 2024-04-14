package com.zhixing.service;

import com.zhixing.entity.PageResult;
import com.zhixing.entity.QueryPageBean;
import com.zhixing.pojo.CheckItem;

import java.util.List;

//服务接口
public interface CheckItemService {
    //添加检查项
    public  void  add(CheckItem checkItem);

    //分页查询检查项
    public PageResult pageQuery(QueryPageBean queryPageBean);

    //删除检查项
    public void deleteById(Integer id);

    //编辑检查项
    public void edit(CheckItem checkItem);

    //回显检查项数据
    public CheckItem findById(Integer id);

    //查询所有检查项
    public List<CheckItem> findAll();
}
























