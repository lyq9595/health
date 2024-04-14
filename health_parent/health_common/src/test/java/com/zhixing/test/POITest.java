package com.zhixing.test;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class POITest {


//
//    //使用poi读取excel
//    @Test
//    public void test1() throws Exception {
//        //加载指定文件 创建一个Excel对象（工作簿）
//        XSSFWorkbook excel=new XSSFWorkbook(new FileInputStream(new File("d:\\poi.xlsx")));
//        // 读取excel文件中第一个sheet标签页
//        XSSFSheet sheet = excel.getSheetAt(0);
//        //遍历sheet标签页 获得每一行数据
//        for (Row row : sheet) {
//            // 遍历行，获得每个单元格对象
//            for (Cell cell : row) {
//                System.out.println(cell.getStringCellValue());
//            }
//        }
//        excel.close();
//    }
//
//    //使用poi读取excel数据②
//    @Test
//    public void test2() throws Exception {
//        //加载指定文件 创建一个Excel对象（工作簿）
//        XSSFWorkbook excel=new XSSFWorkbook(new FileInputStream(new File("d:\\poi.xlsx")));
//        // 读取excel文件中第一个sheet标签页
//        XSSFSheet sheet = excel.getSheetAt(0);
//        //获得当前工作表中最后一个行号  从0开始
//        int lastRowNum = sheet.getLastRowNum();
//        for (int i=0;i<=lastRowNum;i++){
//            XSSFRow row = sheet.getRow(i);//根据行号获取每一行
//            //获取当前行最后一个单元格的索引
//            short lastCellNum = row.getLastCellNum();
//            for (int j=0;j<lastCellNum;j++){
//                XSSFCell cell = row.getCell(j);//获取单元格
//                System.out.println(cell.getStringCellValue());
//            }
//        }
//        excel.close();
//    }
//
//
//
//    //使用poi写入excel数据，通过输出流将创建的excel文件保存到本地磁盘
//    @Test
//    public void test3() throws Exception {
//        //在内存中创建一个excel文件
//        XSSFWorkbook excel=new XSSFWorkbook();
//        //创建一个工作表对象
//        XSSFSheet sheet = excel.createSheet("POIcreate");
//        //在工作表中创建行对象
//        XSSFRow title = sheet.createRow(0);
//        //在行中创建单元格对象
//        title.createCell(0).setCellValue("姓名");
//        title.createCell(1).setCellValue("地址");
//        title.createCell(2).setCellValue("年龄");
//
//        XSSFRow dataRow= sheet.createRow(1);
//        dataRow.createCell(0).setCellValue("小明");
//        dataRow.createCell(1).setCellValue("西安");
//        dataRow.createCell(2).setCellValue("20");
//
//        //创建输出流将内存中的excel文件写到磁盘
//        FileOutputStream out=new FileOutputStream(new File("d:\\hello.xlsx"));
//        excel.write(out);
//        out.flush();
//        excel.close();
//    }

}





















