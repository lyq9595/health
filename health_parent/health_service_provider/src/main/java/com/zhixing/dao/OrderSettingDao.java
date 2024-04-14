package com.zhixing.dao;

import com.zhixing.pojo.OrderSetting;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;
import java.util.Map;


public interface OrderSettingDao {

    @Select("insert into t_ordersetting" +
            "(orderDate,number,reservations)" +
            "values" +
            "(#{orderDate},#{number},#{reservations})")
    //添加预约
    public void add(OrderSetting data);

    @Update("update t_ordersetting set number = #{number} where orderDate = #{orderDate}")
    //根据日期改可预约认数
    public void editNumberByOrderDate(OrderSetting orderSetting);

    @Select("select count(id) from t_ordersetting where orderDate = #{orderDate}")
    //查询日期是否有数据
    public long findCountByOrderDate(Date orderDate);

    @Select("select * from t_ordersetting where orderDate between #{begin} and #{end} ")
    //根据月份查询对应的预约设置数据
    List<OrderSetting> getOrderSettingByMonth(Map<String, String> map);


    //根据日期查询
    @Select("select * from t_ordersetting where orderDate = #{orderDate}")
    OrderSetting findByOrderDate(Date parseString2Date);

    //根据预约日期更新已预约人数
    @Update("update t_ordersetting set reservations = #{reservations} where orderDate = #{orderDate}")
    public void editReservationsByOrderDate(OrderSetting orderSetting);
}



















