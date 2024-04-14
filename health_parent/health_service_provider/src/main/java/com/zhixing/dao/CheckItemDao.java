package com.zhixing.dao;


import com.github.pagehelper.Page;
import com.zhixing.entity.PageResult;
import com.zhixing.entity.QueryPageBean;
import com.zhixing.pojo.CheckItem;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;


public interface CheckItemDao {
    /*插入检查数据*/
    @Insert("insert into t_checkitem(code,name,sex,age,price,type,remark,attention) " +
            " values " +
            " (#{code},#{name},#{sex},#{age},#{price},#{type},#{remark},#{attention})")
    public  void  add(CheckItem checkItem);

    //分页查询检查项
    @Select("<script>select * from t_checkitem " +
            "<where>" +
            "<if test='value != null and value.length>0'>" +
            "and code = #{value} or name = #{value}" +
            "</if>" +
            "</where>" +
            "</script>")
    public Page<CheckItem> selectByCondition(String queryString);

    //根据检查项id查询检查项是否已经关联到了检查组
    @Select("select count(*) from t_checkgroup_checkitem where checkitem_id=#{checkitem_id}")
    public long findCountByCheckItemId(Integer id);

    //删除检查项
    @Delete("delete from t_checkitem where id=#{id}")
    public void deleteById(Integer id);

    @Update("<script>" +
            "update t_checkitem " +
            "<set>" +
            "<if test='name != null'>" +
            "name = #{name}," +
            "</if>" +
            "<if test='sex != null'>" +
            "sex = #{sex}," +
            "</if>" +
            "<if test='code != null'>" +
            "code = #{code}," +
            "</if>" +
            "<if test='age != null'>" +
            "age = #{age}," +
            "</if>" +
            "<if test='price != null'>" +
            "price = #{price}," +
            "</if>" +
            "<if test='type != null'>" +
            "type = #{type}," +
            "</if>" +
            "<if test='attention != null'>" +
            "attention = #{attention}," +
            "</if>" +
            "<if test='remark != null'>" +
            "remark = #{remark}," +
            "</if>" +
            "</set>" +
            "where id=#{id}" +
            "</script>")
    public void edit(CheckItem checkItem);

    //根据id查询检查项
    @Select("select * from t_checkitem where id = #{id}")
    CheckItem findById(Integer id);

    //查询所有的检查项
    @Select("select * from t_checkitem")
    List<CheckItem> findAll();


    @Select("select * from t_checkitem" +
            " where id in " +
            " (select checkitem_id from t_checkgroup_checkitem where checkgroup_id=#{id})")
    //根据检查组id查询关联的检查项
    CheckItem findCheckItemById(int id);
}
























