package com.zhixing.dao;

import com.github.pagehelper.Page;
import com.zhixing.pojo.CheckGroup;
import com.zhixing.pojo.Setmeal;
import org.apache.ibatis.annotations.*;
import org.apache.zookeeper.data.Id;

import java.util.List;
import java.util.Map;

public interface SetmealDao {

    //新增套餐数据
    @Insert("insert into t_setmeal(code,name,sex,age,helpCode,price,remark,attention,img)" +
            "values" +
            "(#{code},#{name},#{sex},#{age},#{helpCode},#{price},#{remark},#{attention},#{img})")
    //获取自增的id
    @SelectKey(statement="select last_insert_id()", keyProperty="id", before = false, resultType=Integer.class)
    public void add(Setmeal setmeal);

    //设置检查组和套餐多对多关系
    @Insert("insert into t_setmeal_checkgroup(setmeal_id,checkgroup_id) " +
            "values" +
            " (#{setmealId},#{checkgroupId})")
    public void setMealAndCheckGroup(Map map);



    //根据条件分页查询套餐
    @Select( "<script>select * from t_setmeal " +
            "<where>" +
            "<if test='value != null and value.length>0'>" +
            "and code = #{value} or name = #{value} and helpCode=#{value}" +
            "</if>" +
            "</where>" +
            "</script>")

    Page<CheckGroup> findByCondition(String queryString);


    //查询所有套餐
    @Select("select * from t_setmeal")
    List<Setmeal> findAll();



    //根据套餐ID查询套餐详情（套餐基本信息、套餐对应的检查组）
    @Select("select * from t_setmeal where  id=#{id}")
    @Results({
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "code", column = "code"),
            @Result(property = "helpCode", column = "helpCode"),
            @Result(property = "sex", column = "sex"),
            @Result(property = "age", column = "age"),
            @Result(property = "price", column = "price"),
            @Result(property = "remark", column = "remark"),
            @Result(property = "attention", column = "attention"),
            @Result(property = "img", column = "img"),
            @Result(property = "checkGroups",column = "id",javaType = java.util.List.class,
                    many = @Many(select = "com.zhixing.dao.CheckGroupDao.findCheckGroupById"))
    })
    Setmeal findById(int id);


    //套餐预约占比饼形图
    @Select("select s.name,COUNT(o.id) value " +
            "from t_order o,t_setmeal s where o.setmeal_id=s.id GROUP BY s.name")
    List<Map<String, Object>> findSetmealCount();

}
















