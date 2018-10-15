package com.weixin.controller;

import com.weixin.service.CoreService;
import com.weixin.util.SignUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

/**
 * Created by LIU on 2018/10/10 15:29
 */
@Controller
public class connectionController {

    @Autowired
    private CoreService coreService;

    @RequestMapping("/weixin")
    @ResponseBody
    public void weixinConnection(String signature, String timestamp, String nonce, String echostr, HttpServletResponse response, HttpServletRequest request){
        PrintWriter out = null;

        try {
            response.setContentType("text/json;charset=utf-8");
            out = response.getWriter();
            if (SignUtil.checkSignature(signature, timestamp, nonce)) {
                if (null != echostr && !"".equals(echostr)) {
                    System.out.println("签名校验成功");
                    out.write(echostr);
                }else{
                    //处理用户消息
                    InputStream inputStream = request.getInputStream();
                    String respXml = coreService.processRequest(inputStream, signature);
                    System.out.println("intoUserCenter:returnMsg:===>>signature=" + signature + ",[" + respXml + "]");
                    out.write(respXml);
                }
            } else {
                System.out.println("签名校验失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
