package com.zhixing.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhixing.constant.MessageConstant;
import com.zhixing.entity.Result;
import com.zhixing.service.MemberService;
import com.zhixing.service.ReportService;
import com.zhixing.service.SetmealService;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

//报表
@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private MemberService memberService;

    @Reference
    private SetmealService service;

    @Reference
    private ReportService reportService;

    //会员数量折线图数据
    @RequestMapping("/getMemberReport")
    public Result getMemberReport(){
        Map<String,Object> map=new HashMap<>();
        List<String> month=new ArrayList<>();

        Calendar calendar=Calendar.getInstance();//获得日历对象 默认时间是当前系统时间
        //计算过去一年的12个月
        calendar.add(Calendar.MONTH,-12);//获得当前时间往前推12个月
        for (int i=0;i<12;i++){
            calendar.add(Calendar.MONTH,1);//获得当前时间往前推12个月
            Date date= calendar.getTime();
            month.add(new SimpleDateFormat("yyyy.MM").format(date));
        }
        map.put("months",month);

        //查询月认数
        List<Integer> memberCount=memberService.findMemberCountByMonths(month);
        map.put("memberCount",memberCount);
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
    }


    //套餐预约占比饼形图
    @GetMapping("/getSetmealReport")
    public Result getSetmealReport(){
        //使用模拟数据测试使用什么样子的java对象转换为所需的json数据格式
        Map<String,Object> data=new HashMap<>();
        /*//套餐名称查询
        List<String> setmealNames=new ArrayList<>();
        setmealNames.add("直接访问");
        setmealNames.add("邮件营销");*/
        try {
            List<Map<String,Object>> setmealCount=service.findSetmealCount();//套餐名称及占比
            data.put("setmealCount",setmealCount);
            List<String> setmealNames=new ArrayList<>();//套餐名称
            for (Map<String, Object> map : setmealCount) {
                String name = (String) map.get("name");//套餐名称
                setmealNames.add(name);
            }

            data.put("setmealNames",setmealNames);
            return new Result(true,MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,data);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }

    }

    //导出运营数据
    @GetMapping("/getBusinessReportData")
    public Result getBusinessReportData(){
        //使用模拟数据测试是否可以正常展示
        /*data.put("reportDate","2021-04-25");//报表时间
        data.put("todayNewMember",343);//报表时间
        data.put("totalMember",125);//总人数
        data.put("thisWeekNewMember",125);//本周新会员
        data.put("thisMonthNewMember",125);//本月会员
        data.put("todayOrderNumber",125);//今日预约
        data.put("todayVisitsNumber",125);//今日到诊
        data.put("thisWeekOrderNumber",125);//本周预约
        data.put("thisWeekVisitsNumber",125);//本周到诊
        data.put("thisMonthOrderNumber",125);//本月预约
        data.put("thisMonthVisitsNumber",125);//本月到诊

        List<Map<String,Object>> hotSetmeal=new ArrayList();
        Map<String ,Object> map1=new HashMap<>();
        map1.put("name","阳光爸妈升级肿瘤12项筛查（男女单人）体检套餐");
        map1.put("setmeal_count",300);
        map1.put("proportion",0.33);

        Map<String ,Object> map2=new HashMap<>();
        map2.put("name","阳光爸妈升级肿瘤12项筛查体检套餐");
        map2.put("setmeal_count",300);
        map2.put("proportion",0.33);
        System.out.println(map2);

        hotSetmeal.add(map1);
        hotSetmeal.add(map2);
        data.put("hotSetmeal",hotSetmeal);*/


        try {
            Map<String,Object> data = reportService.getBusinessReportData();
            return new Result(true,MessageConstant.GET_BUSINESS_REPORT_SUCCESS,data);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }



    //导出运营数据
    @RequestMapping("/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response){
        try {
            Map<String,Object> result = reportService.getBusinessReportData();
            //取出返回结果数据，准备将报表数据写入到Excel文件中
            String reportDate = (String) result.get("reportDate");
            Integer todayNewMember = (Integer) result.get("todayNewMember");
            Integer totalMember = (Integer) result.get("totalMember");
            Integer thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");


            String filePath=request.getSession().getServletContext().getRealPath("template")+File.separator+"report_template.xlsx";
            //基于提供的Excel模板文件在内存中创建一个excel对象
            XSSFWorkbook excel=new XSSFWorkbook(new FileInputStream(new File(filePath)));
            //读取第一个工作表
            XSSFSheet sheet = excel.getSheetAt(0);
            //第三行第6列报表日期  下同类似
            XSSFRow row = sheet.getRow(2);
            XSSFCell cell = row.getCell(5);
            cell.setCellValue(reportDate);
            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);//新增会员数
            row.getCell(7).setCellValue(totalMember);//总会员数
            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数
            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日到诊数
            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周到诊数
            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月到诊数

            int rowNum=12;
            for(Map map : hotSetmeal){
                //热门套餐
                String name = (String) map.get("name");
                Long setmeal_count = (Long) map.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) map.get("proportion");
                row = sheet.getRow(rowNum ++);
                row.getCell(4).setCellValue(name);//套餐名称
                row.getCell(5).setCellValue(setmeal_count);//预约数量
                row.getCell(6).setCellValue(proportion.doubleValue());//占比
            }

            //user OutputStream 使用输出流进行表格下载,基于浏览器作为客户端下载
            OutputStream out=response.getOutputStream();
            //set response header type, make the client aware of the file type
            response.setContentType("application/vnd.ms-excel");//represent the excel file type
            response.setHeader("content-Disposition",
                    "attachment;filename=report.xlsx");//down load by attachment form


            excel.write(out);
            out.flush();
            out.close();
            excel.close();
            return null;
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }


    //导出运营数据到pdf并提供客户端下载
    @RequestMapping("/exportBusinessReport4PDF")
    public Result exportBusinessReport4PDF(HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, Object> result = reportService.getBusinessReportData();
        //取出返回结果数据，准备将报表数据写入到PDF文件中
            List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");
        //动态获取模板文件绝对磁盘路径
            String jrxmlPath =
                    request.getSession().getServletContext().getRealPath("template") +
                            File.separator + "/health_business3.jrxml";
            String jasperPath =
                    request.getSession().getServletContext().getRealPath("template") +
                            File.separator + "health_business3.jasper";
        //编译模板
            JasperCompileManager.compileReportToFile(jrxmlPath, jasperPath);
        //填充数据---使用JavaBean数据源方式填充
            JasperPrint jasperPrint =
                    JasperFillManager.fillReport(jasperPath,result,
                            new JRBeanCollectionDataSource(hotSetmeal));

            ServletOutputStream out = response.getOutputStream();
            response.setContentType("application/pdf");
            response.setHeader("content-Disposition",
                    "attachment;filename=report.pdf");
        //输出文件
            JasperExportManager.exportReportToPdfStream(jasperPrint,out);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

}






























