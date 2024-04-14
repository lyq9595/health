package com.zhixing.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zhixing.constant.MessageConstant;
import com.zhixing.dao.MemberDao;
import com.zhixing.dao.OrderDao;
import com.zhixing.dao.OrderSettingDao;
import com.zhixing.entity.Result;
import com.zhixing.pojo.Member;
import com.zhixing.pojo.Order;
import com.zhixing.pojo.OrderSetting;
import com.zhixing.service.OrderService;
import com.zhixing.utils.DateUtils;
import org.junit.After;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;

/*
    体检预约服务
* */
@Service(interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderSettingDao orderSettingDao;


    @Autowired
    private MemberDao memberDao;


    @Autowired
    private OrderDao orderDao;
    //体检预约
    @Override
    public Result order(Map map) throws Exception {
        //1.检查预约日期是否进行了预约设置
        String orderDate = (String) map.get("orderDate");
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(DateUtils.parseString2Date(orderDate));
        if (orderSetting==null){
            //指定日期没有进行预约设置
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }

        //2.检查用户所选择的预约日期是否已满
        int number = orderSetting.getNumber();//可预约认数
        int reservations = orderSetting.getReservations();//已经预约的认数
        if (reservations>=number){
            return new Result(false,MessageConstant.ORDER_FULL);
        }

        //3.检查用户是否重复预约（同一个用户 同一天 同一个套餐）
        String telephone = (String) map.get("telephone");
        Member member = memberDao.findByTelephone(telephone);
        if (member!=null){
            //判断是否在重复预约
            Integer memberId = member.getId();
            Date order_date=DateUtils.parseString2Date(orderDate);//预约日期
            String setmealId = (String) map.get("setmealId");//套餐ID
            Order order = new Order(memberId, order_date, Integer.parseInt(setmealId));
            //根据条件进行查询
            List<Order> list = orderDao.findByCondition(order);
            if (list!=null && list.size()>0){
                //说明用户在重复预约，无法完成再次预约
                return new Result(false,MessageConstant.HAS_ORDERED);
            }
        }else {
            //4.检查当前用户是否为会员 是会员预约 不是会员自动注册预约
            member= new Member();
            member.setName((String) map.get("name"));
            member.setPhoneNumber(telephone);
            member.setIdCard((String) map.get("idCard"));
            member.setSex((String) map.get("sex"));
            member.setRegTime(new Date());
            memberDao.add(member);//自动完成会员注册


        }

        //5.预约成功 更新当日预约认数
        Order order=new Order();
        order.setMemberId(member.getId());//设置会员id 注解会返回
        order.setOrderDate(DateUtils.parseString2Date(orderDate));
        order.setOrderType((String) map.get("orderType"));
        order.setOrderStatus(Order.ORDERSTATUS_NO);//到诊状态
        order.setSetmealId(Integer.parseInt((String) map.get("setmealId")));
        orderDao.add(order);
        orderSetting.setReservations(orderSetting.getReservations()+1);//设置已预约人数+1
        orderSettingDao.editReservationsByOrderDate(orderSetting);
        return new Result(true,MessageConstant.ORDER_SUCCESS,order.getId());
    }

    //根据预约id查询预约相关信息（姓名member表 日期order表 套餐名称setmeal表 预约类型order表）
    @Override
    public Map findById(Integer id) throws Exception {
        Map map = orderDao.findById4Detail(id);
        if (map != null) {
            Date orderDate = (Date) map.get("orderDate");
            map.put("orderDate",DateUtils.parseDate2String(orderDate));
        }

        return map;
    }
}


























