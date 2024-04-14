package com.zhixing.service;

import com.sun.jersey.core.header.OutBoundHeaders;
import com.zhixing.pojo.Member;

import java.util.List;

public interface MemberService {

    //根据手机号查会员
    public Member findByTelephone(String telephone);

    //自动注册会员
    public  void add(Member member);


    //根据月份查询会员人数
    public List<Integer> findMemberCountByMonths(List<String> months);
}

















