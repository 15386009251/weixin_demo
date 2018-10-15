package com.weixin.service;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weixin.bean.text.TextMessage;
import com.weixin.util.ConstantParam;
import com.weixin.util.Dom4jUtil;
import com.weixin.util.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 核心服务类
 */
@Service
public class CoreService {

    public static String TAG = "WeChatService";
    private static ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public WxTokenGetService wxTokenGetService;

    /**
     * 处理微信发来的请求
     *
     * @param inputStream
     * @return xml
     * @throws FileNotFoundException
     */
    public String processRequest(InputStream inputStream, String signature) {
        Date utilDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        // xml格式的消息数据
        String respXml = null;
        try {
            // 调用parseXml方法解析请求消息
            Map<String, String> requestMap = Dom4jUtil.xmlToMap(inputStream);
            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(requestMap);
            System.out.println("reqMsg:===>>signature=" + signature + ",[" + jsonObject.toString() + "]");
            // 发送方帐号
            String fromUserName = requestMap.get("FromUserName");
            // 开发者微信号
            String toUserName = requestMap.get("ToUserName");
            String createTime = requestMap.get("CreateTime");//消息创建时间
            // 消息类型
            String event = requestMap.get("Event");
            // event
            String msgType = requestMap.get("MsgType");
            // key类型
            String eventKey = requestMap.get("EventKey");
            // key类型
            String ticket = requestMap.get("Ticket");

            MessageType type = MessageType.valueOf(MessageType.class, msgType);


            if (MessageType.event == type) {
                if ("subscribe".equals(event)) {
                    respXml = Dom4jUtil.xmlToString(Dom4jUtil.getXmlByBean(userSubscribe(event, fromUserName, toUserName)), "UTF-8", false);
                    String userName = wxTokenGetService.getUserInfo(fromUserName);
                    respXml = respXml.replaceAll("\\{USERNAME\\}", userName);
                    return respXml;
                } else if ("unsubscribe".equals(event)) {

                } else if ("CLICK".equals(event)) {
                    if ("XXXX".equals(eventKey)) {

                    }
                }

            } else if (MessageType.text == type) {
                String content = requestMap.get("Content");
                respXml = Dom4jUtil.xmlToString(Dom4jUtil.getXmlByBean(userSubscribe(content, fromUserName, toUserName)), "UTF-8", false);
                return respXml;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 处理文本消息
     **/
    public TextMessage userSubscribe(String str, String fromUserName, String toUserName) {
        TextMessage textMessage = new TextMessage();
        textMessage.setToUserName(fromUserName);
        textMessage.setFromUserName(toUserName);
        textMessage.setCreateTime(new Date().getTime() + "");
        textMessage.setMsgType(Dom4jUtil.MESSAGE_TYPE_TEXT);
        textMessage.setMsgId(new Date().getTime() + "");
        String res = ConstantParam.findResponseText(str);
        if (res == null || res.equals("")) {
            res = ConstantParam.findResponseText("else");
        }
        textMessage.setContent(res);
        return textMessage;
    }
}