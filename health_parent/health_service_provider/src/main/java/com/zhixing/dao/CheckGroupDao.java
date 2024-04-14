package com.zhixing.dao;

import com.github.pagehelper.Page;
import com.zhixing.pojo.CheckGroup;
import com.zhixing.pojo.CheckItem;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface CheckGroupDao {

    //新增检查组 操作t_checkgroup表
    @Insert("insert into t_checkgroup(code,name,sex,helpCode,remark,attention)" +
            "values" +
            "(#{code},#{name},#{sex},#{helpCode},#{remark},#{attention})")
    //获取自增的id
    @SelectKey(statement="select last_insert_id()", keyProperty="id", before = false, resultType=Integer.class)
    public void add(CheckGroup checkGroup);

    //设置检查组和检查项多对多关系
    @Insert("insert into t_checkgroup_checkitem(checkgroup_id,checkitem_id) " +
            "values" +
            " (#{checkgroup_id},#{checkitem_id})")
    public void setCheckAndCheckItem(Map map);

    //根据条件分页查询检查组
    @Select( "<script>select * from t_checkgroup " +
            "<where>" +
            "<if test='value != null and value.length>0'>" +
            "and code = #{value} or name = #{value} and helpCode=#{value}" +
            "</if>" +
            "</where>" +
            "</script>")

    Page<CheckGroup> findByCondition(String queryString);

    //根据id回显检查组基本信息
    @Select("select * from t_checkgroup where id = #{id}")
    CheckGroup findById(Integer id);

    //查询检查组中包含的检查项id
    @Select("select checkitem_id from t_checkgroup_checkitem where checkgroup_id = #{id}")
    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    //根据id动态编辑检查组信息
    @Update("<script>" +
            "update t_checkgroup " +
            "<set>" +
            "<if test=\"name != null\">\n" +
            "name = #{name},\n" +
            "</if>\n" +
            "<if test=\"sex != null\">\n" +
            "sex = #{sex},\n" +
            "</if>\n" +
            "<if test=\"code != null\">\n" +
            "code = #{code},\n" +
            "</if>\n" +
            "<if test=\"helpCode != null\">\n" +
            "helpCode = #{helpCode},\n" +
            "</if>\n" +
            "<if test=\"attention != null\">\n" +
            "attention = #{attention},\n" +
            "</if>\n" +
            "<if test=\"remark != null\">\n" +
            "remark = #{remark},\n" +
            "</if>" +
            "</set>" +
            "where id=#{id}" +
            "</script>")
    void edit(CheckGroup checkGroup);

    //清理检查项中间表
    @Delete("delete from t_checkgroup_checkitem where checkgroup_id=#{id}")
    void deleteAssociation(Integer id);

    //根据id清理检查组
    @Delete("delete from t_checkgroup where id=#{id}")
    void deleteCheckGroup(Integer id);

    //查询所有检查组
    @Select("select * from t_checkgroup")
    List<CheckGroup> findAll();


    //根据套餐id查询关联的检查组详情
    @Select("select * from t_checkgroup" +
            " where id in " +
            " (select checkgroup_id from t_setmeal_checkgroup where setmeal_id=#{setmeal_id})")
    @Results({
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "code", column = "code"),
            @Result(property = "helpCode", column = "helpCode"),
            @Result(property = "sex", column = "sex"),
            @Result(property = "remark", column = "remark"),
            @Result(property = "attention", column = "attention"),
            @Result(property = "checkItems",column = "id",javaType = java.util.List.class,
                    many = @Many(select = "com.zhixing.dao.CheckItemDao.findCheckItemById"))
    })
    CheckGroup findCheckGroupById(int setmeal_id);

}
















