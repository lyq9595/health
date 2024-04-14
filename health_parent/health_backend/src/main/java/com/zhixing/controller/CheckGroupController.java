package com.zhixing.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhixing.constant.MessageConstant;
import com.zhixing.entity.PageResult;
import com.zhixing.entity.QueryPageBean;
import com.zhixing.entity.Result;
import com.zhixing.pojo.CheckGroup;
import com.zhixing.service.CheckGroupService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 检查组管理
 */
@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {

    @Reference
    private CheckGroupService checkGroupService;



    //新增检查组
    @RequestMapping("/add")
    public Result add(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds){
        try{
            checkGroupService.add(checkGroup,checkitemIds);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    //分页查询检查组
    @PostMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        return checkGroupService.pageQuery(queryPageBean);
    }

    //根据id回显检查组基本信息
    @GetMapping("/findById")
    public Result findById(Integer id) {
        try{
           CheckGroup checkGroup = checkGroupService.findById(id);
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroup);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }

    }


    //查询检查组中包含的检查项id
    @GetMapping("/findCheckItemIdsByCheckGroupId")
    public Result findCheckItemIdsByCheckGroupId(Integer id) {
        try{
            List<Integer> checkItemIds = checkGroupService.findCheckItemIdsByCheckGroupId(id);
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItemIds);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKITEM_SUCCESS);
        }

    }

    //提交修改检查组
    @PostMapping("/edit")
    public Result edit(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds) {
        try{
            checkGroupService.edit(checkGroup,checkitemIds);
            return new Result(true, MessageConstant.EDIT_CHECKGROUP_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_CHECKGROUP_FAIL);
        }

    }

    //删除检查组
    @DeleteMapping("/delete")
    public Result delete(Integer id) {
        try{
            checkGroupService.delete(id);
            return new Result(true, MessageConstant.DELETE_CHECKGROUP_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_CHECKGROUP_FAIL);
        }

    }

    //查询所有检查组
    @GetMapping("/findAll")
    public Result findAll() {
        try{
           List<CheckGroup> list = checkGroupService.findAll();
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,list);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }

    }
}















