package com.weixin.controller;

import com.weixin.service.WxTokenGetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by LIU on 2018/10/12 9:59
 */
@Controller
public class AccessTokenController {

    @Autowired
    WxTokenGetService wxTokenGetService;

    @RequestMapping("/accessToken")
    public void init() {
        System.out.println("开始获取accessToken");
        String accessToken = wxTokenGetService.tokenReq();
        System.out.println("获取的accessToken为："+accessToken);
    }
}
