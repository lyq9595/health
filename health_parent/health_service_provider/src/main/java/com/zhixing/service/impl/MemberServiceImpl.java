package com.zhixing.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.zhixing.dao.MemberDao;
import com.zhixing.pojo.Member;
import com.zhixing.service.MemberService;
import com.zhixing.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;

    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    @Override
    public void add(Member member) {
        String password = member.getPassword();
        if (password!=null){
            //md5加密
            password = MD5Utils.md5(password);
            member.setPassword(password);
        }
        memberDao.add(member);
    }


    //根据月份查询会员人数
    @Override
    public List<Integer> findMemberCountByMonths(List<String> months) {
        List<Integer> memberCount=new ArrayList<>();
        for (String month : months) {
            String date=month+".31";//2018.05.31
            Integer count = memberDao.findMemberCountBeforeDate(date);
            memberCount.add(count);
        }

        return memberCount;
    }


}





















