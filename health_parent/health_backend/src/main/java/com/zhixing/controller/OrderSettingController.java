package com.zhixing.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhixing.constant.MessageConstant;
import com.zhixing.entity.Result;
import com.zhixing.pojo.Order;
import com.zhixing.pojo.OrderSetting;
import com.zhixing.service.OrderSettingService;
import com.zhixing.utils.POIUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 预约设置
 *
 */
@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;


    //文件上传 实现预约设置数据批量导入
    @RequestMapping("/upload")
    public Result upload(@RequestParam("excelFile") MultipartFile excelFile){
        try {
            //poi解析表格数据
            List<String[]> list = POIUtils.readExcel(excelFile);
            List<OrderSetting> data=new ArrayList<>();
            for (String[] strings : list) {
                String orderDate=strings[0];
                String number=strings[1];
                OrderSetting orderSetting = new OrderSetting(new Date(orderDate),Integer.parseInt(number));
                data.add(orderSetting);
            }
            //通过dubbo远程调用服务实现数据批量导入到数据库
            orderSettingService.add(data);
            return  new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
            //文件解析失败
            return  new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }

    }

    //根据月份查询对应的预约设置数据
    @PostMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String date){
        try {
            List<Map> list=orderSettingService.getOrderSettingByMonth(date);
            return  new Result(true, MessageConstant.GET_ORDERSETTING_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            //文件解析失败
            return  new Result(false, MessageConstant.GET_ORDERSETTING_FAIL);
        }

    }

    //设置预约认数
    @PostMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting){
        try {
            orderSettingService.editNumberByDate(orderSetting);
            return  new Result(true, MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            //文件解析失败
            return  new Result(false, MessageConstant.ORDERSETTING_FAIL);
        }

    }

}





















