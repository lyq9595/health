package com.zhixing.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zhixing.dao.CheckGroupDao;
import com.zhixing.entity.PageResult;
import com.zhixing.entity.QueryPageBean;
import com.zhixing.pojo.CheckGroup;
import com.zhixing.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;

    //新增检查组 同时需要让检查项关联检查组
    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        //新增检查组 操作t_CheckGroup
        checkGroupDao.add(checkGroup);
        //设置检查组和检查项多对多的关联关系t_checkgroup_checkitem
        setCheckAndCheckItem(checkGroup, checkitemIds);
    }

    //分页查询检查组
    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        //分页插件助手
        PageHelper.startPage(currentPage,pageSize);
        Page<CheckGroup> page =checkGroupDao.findByCondition(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    //根据id回显检查组基本信息
    @Override
    public CheckGroup findById(Integer id) {
        return  checkGroupDao.findById(id);
    }

    //查询检查组中包含的检查项id
    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
        return checkGroupDao.findCheckItemIdsByCheckGroupId(id);

    }

    //提交修改检查组
    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkitemIds) {
        //修改检查组基本信息，t_checkgroup
        checkGroupDao.edit(checkGroup);
        //清楚当前检查组关联的检查项 中间关系表
        checkGroupDao.deleteAssociation(checkGroup.getId());
        //重新建立关联关系
        setCheckAndCheckItem(checkGroup, checkitemIds);
    }

    @Override
    public void delete(Integer id) {
        checkGroupDao.deleteAssociation(id);
        checkGroupDao.deleteCheckGroup(id);
    }

    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }

    //设置检查组和检查项多对多的关联关系t_checkgroup_checkitem
    private void setCheckAndCheckItem(CheckGroup checkGroup, Integer[] checkitemIds) {
        Integer checkGroupId = checkGroup.getId();
        if (checkitemIds != null && checkitemIds.length > 0) {
            for (Integer checkitemId : checkitemIds) {
                Map<String, Integer> map = new HashMap<>();
                map.put("checkgroup_id", checkGroupId);
                map.put("checkitem_id", checkitemId);
                checkGroupDao.setCheckAndCheckItem(map);
            }
        }
    }
}



























