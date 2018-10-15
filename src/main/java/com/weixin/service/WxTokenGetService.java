package com.weixin.service;

import com.alibaba.fastjson.JSONObject;
import com.weixin.bean.TokenBean;
import com.weixin.util.ConstantParam;
import com.weixin.util.HttpClient;
import com.weixin.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WxTokenGetService {

    private final static Logger logger = LoggerFactory.getLogger(WxTokenGetService.class);

    @Autowired
    RedisUtil redisUtil;

    /**
     * 封装获取请求token，并放入redis缓存
     **/
    public String tokenReq() {
        String accessToken = "";
        Object obtokenBean = redisUtil.get(ConstantParam.tokenKey);
        TokenBean bean = new TokenBean();
        if (obtokenBean == null) {
            bean = getToken();
            if (bean != null) {
                accessToken = bean.getToken();
            }
        } else {
            bean = (TokenBean) obtokenBean;
            long timestamp = System.currentTimeMillis();
            int expireTime = bean.getExpiresIn() * 1000;
            if (bean != null && ((timestamp - bean.getTimestamp()) <= expireTime)) {
                logger.warn("WxCard  getExpiresIn  " + (timestamp - bean.getTimestamp()) + " getExpiresIn " + expireTime);
                accessToken = bean.getToken();
            } else {
                bean = getToken();
                if (bean != null) {
                    accessToken = bean.getToken();
                }
            }
        }
        return accessToken;
    }

    public static void main(String[] args) {
        System.out.println(72000/60/60);
    }

    /**
     * 获取微信公众号token
     **/
    public TokenBean getToken() {
        logger.info("tokenReq req = " + ConstantParam.weiChatUrl);
        TokenBean bean = new TokenBean();
        String accessToken = "";
        String obj = HttpClient.getAccessToken(ConstantParam.weiChatUrl);
        if (null != obj) {
            JSONObject response = JSONObject.parseObject(obj);
            accessToken = response.getString("access_token");
            bean.setExpiresIn(Integer.valueOf(response.getString("expires_in")));
            bean.setToken(accessToken);
            bean.setTimestamp(System.currentTimeMillis());
            redisUtil.remove(ConstantParam.tokenKey);
            redisUtil.set(ConstantParam.tokenKey, bean, bean.getExpiresIn() - 200L);
            logger.info("tokenReq response= " + response);
        }
        return bean;
    }

    /**
     * 根据openId获取用户的基本信息字段
     **/
    public String getUserInfo(String openId) {
        String userInfoUrl = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + tokenReq() + "&openid=" + openId + "&lang=zh_CN";
        logger.error("getUserInfo req = " + userInfoUrl);
        String obj = HttpClient.getAccessToken(userInfoUrl);
        JSONObject response = JSONObject.parseObject(obj);
        String username = response.getString("nickname");
        logger.info("getUserInfo response= " + response.toJSONString());
        return username;
    }
}
