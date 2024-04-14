package com.zhixing.service;

import com.zhixing.entity.PageResult;
import com.zhixing.entity.QueryPageBean;
import com.zhixing.pojo.CheckGroup;

import java.util.List;

public interface CheckGroupService {

    //新增检查组
    public void add(CheckGroup checkGroup,Integer[] checkitemIds);

    //分页查询检查组
    PageResult pageQuery(QueryPageBean queryPageBean);

    //根据id回显检查组基本信息
    CheckGroup findById(Integer id);

    //查询检查组中包含的检查项id
    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    //提交修改检查组
    void edit(CheckGroup checkGroup, Integer[] checkitemIds);

    //删除检查组
    void delete(Integer id);

    //查询所有检查组
    List<CheckGroup> findAll();
}














