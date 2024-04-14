package com.zhixing.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zhixing.dao.CheckItemDao;
import com.zhixing.entity.PageResult;
import com.zhixing.entity.QueryPageBean;
import com.zhixing.pojo.CheckItem;
import com.zhixing.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 检查项服务
 */
@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckServiceImpl implements CheckItemService {

    @Autowired
    private CheckItemDao checkItemDao;

    //添加检查项
    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    //检查项分页查询
    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        //完成分页查询基于mybatis的分页助手插件完成
        PageHelper.startPage(currentPage,pageSize);
        Page<CheckItem> page = checkItemDao.selectByCondition(queryString);
        long total = page.getTotal();
        List<CheckItem> rows = page.getResult();
        return new PageResult(total,rows);
    }

    //删除检查项
    @Override
    public void deleteById(Integer id) {
        //判断当前检查项是否已经关联到检查组
        long count = checkItemDao.findCountByCheckItemId(id);
        if (count>0){
            //说明当前检查项已经被关联到检查组了 不允许删除
            new RuntimeException();
        }
        checkItemDao.deleteById(id);

    }

    //编辑检查项
    @Override
    public void edit(CheckItem checkItem) {
        checkItemDao.edit(checkItem);

    }

    //根据id查询检查项
    @Override
    public CheckItem findById(Integer id) {
        return checkItemDao.findById(id);
    }

    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }


}

















