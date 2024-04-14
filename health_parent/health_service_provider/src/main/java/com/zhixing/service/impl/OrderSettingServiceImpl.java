package com.zhixing.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.qiniu.storage.model.FileListing;
import com.zhixing.dao.OrderSettingDao;
import com.zhixing.pojo.Order;
import com.zhixing.pojo.OrderSetting;
import com.zhixing.service.OrderSettingService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 预约设置服务
 */
@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    //批量导入预约设置数据
    @Override
    public void add(List<OrderSetting> list) {
        if (list != null && list.size()>0){
            for (OrderSetting orderSetting : list) {
                //判断当前日期是否已经进行了预约设置
                long countByOrderDate = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
                if (countByOrderDate>0){
                    //已经进行了预约设置 执行更新操作
                    orderSettingDao.editNumberByOrderDate(orderSetting);
                }else {
                    //未进行预约设置 执行插入操作
                    orderSettingDao.add(orderSetting);
                }
            }
        }


    }

    //根据月份查询对应的预约设置数据
    @Override
    public List<Map> getOrderSettingByMonth(String date) {//格式：yyyy-MM
        String begin = date+"-1";
        String end = date + "-31";
        Map<String,String> map=new HashMap<>();
        map.put("begin",begin);
        map.put("end",end);
        List<OrderSetting> list = orderSettingDao.getOrderSettingByMonth(map);
//        List<Map> result =new ArrayList<>();
        /*if (!CollectionUtils.isEmpty(list)){
            for (OrderSetting orderSetting : list) {
                Map<String,Object> m=new HashMap<>();
                m.put("date",orderSetting.getOrderDate().getDate());//获取日期数字
                m.put("number",orderSetting.getNumber());
                m.put("reservations",orderSetting.getReservations());
                result.add(m);
            }*/
            List<Map> result =list.stream().map(orderSetting->{
                Map<String,Object> m=new HashMap<>();
                m.put("date",orderSetting.getOrderDate().getDate());//获取日期数字
                m.put("number",orderSetting.getNumber());
                m.put("reservations",orderSetting.getReservations());
                return m;
            }).collect(Collectors.toList());
             return result;

        }

    //根据日期设置预约认数
    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        //根据日期查询是否已经进行了日预约设置
        Date orderDate = orderSetting.getOrderDate();
        long count = orderSettingDao.findCountByOrderDate(orderDate);
        if (count>0){//更新
            orderSettingDao.editNumberByOrderDate(orderSetting);
        }else {
            orderSettingDao.add(orderSetting);
        }


    }

}
















