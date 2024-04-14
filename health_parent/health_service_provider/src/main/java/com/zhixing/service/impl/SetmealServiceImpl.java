package com.zhixing.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zhixing.constant.RedisConstant;
import com.zhixing.dao.SetmealDao;
import com.zhixing.entity.PageResult;
import com.zhixing.entity.QueryPageBean;
import com.zhixing.pojo.CheckGroup;
import com.zhixing.pojo.Setmeal;
import com.zhixing.service.SetmealService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import redis.clients.jedis.JedisPool;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 体检套餐服务
 */
@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private SetmealDao setmealDao;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Value("${out_put_path}")//从属性文件读取输出目录的路径
    private String  outPutPath;



    //新增套餐信息，同时需要关联价差组
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        setmealDao.add(setmeal);
        Integer setmealId = setmeal.getId();
        this.setSetmealAndCheckgroup(setmealId,checkgroupIds);
        String fileName = setmeal.getImg();
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,fileName);

        //当条件套餐后 重新生成静态页面（套餐列表页面、套餐详情页面）
        generateMobileStaticHtml();
    }

    //设置套餐和检查组多对多关系t_setmeal和t_checkgroupt_checkgroup
    public void setSetmealAndCheckgroup(Integer setmealId,Integer[] checkgroupIds){
        if (checkgroupIds != null && checkgroupIds.length>0){
            for (Integer checkgroupId : checkgroupIds) {
                Map<String,Integer> map=new HashMap<>();
                map.put("setmealId",setmealId);
                map.put("checkgroupId",checkgroupId);
                System.out.println(map);
                setmealDao.setMealAndCheckGroup(map);
            }
        }
    }

    //生成当前方法所需的静态页面
    public void generateMobileStaticHtml(){
        //在生成静态界面之前需要查询
        List<Setmeal> list = setmealDao.findAll();
        //需要生成套餐列表静态页面
        generateMobileSetmealListHtml(list);
        //需要生成套餐详情静态页面
        generateMobileSetmealDetailHtml(list);
    }

    //生成套餐列表静态界面
    public  void generateMobileSetmealListHtml(List<Setmeal> list){
        Map map=new HashMap();
        map.put("setmealList",list);
        generateHtml("mobile_setmeal.ftl","m_setmeal.html",map);
    }

    //生成套餐列表详情界面
    public  void generateMobileSetmealDetailHtml(List<Setmeal> list){
        for (Setmeal setmeal : list) {
            Map map=new HashMap();
            map.put("setmeal",setmealDao.findById(setmeal.getId()));
            generateHtml("mobile_setmeal_detail.ftl",
                    "setmeal_detail_"+setmeal.getId()+".html",map);
        }

    }

    //生成静态页面
    public void generateHtml(String templateName,String htmlPageName,Map map){
        Configuration configuration = freeMarkerConfigurer.getConfiguration();//获得配置对象
        Writer out=null;
        try {
            Template template = configuration.getTemplate(templateName);
            //输出流
            out=new FileWriter(new File(outPutPath+"/"+htmlPageName));

            template.process(map,out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        //分页插件助手
        PageHelper.startPage(currentPage,pageSize);
        Page<CheckGroup> page =setmealDao.findByCondition(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    //查询所有
    @Override
    public List<Setmeal> findAll() {
        return setmealDao.findAll();
    }

    //根据套餐ID查询套餐详情（套餐基本信息、套餐对应的检查组）
    @Override
    public Setmeal findById(int id) {
    return setmealDao.findById(id);
    }

    //套餐预约占比饼形图
    @Override
    public List<Map<String, Object>> findSetmealCount() {
        return setmealDao.findSetmealCount();
    }


}






