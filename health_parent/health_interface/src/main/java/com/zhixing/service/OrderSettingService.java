package com.zhixing.service;

import com.zhixing.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

public interface OrderSettingService {

        public void add(List<OrderSetting> data);

        //根据月份查询对应的预约设置数据
        List<Map> getOrderSettingByMonth(String date);

        //设置预约认数
        void editNumberByDate(OrderSetting orderSetting);
}


















