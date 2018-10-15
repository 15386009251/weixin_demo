package com.weixin.util;

import java.util.HashMap;
import java.util.Map;

public class ConstantParam {
    public final static String appid = "wx97d4cbfdb9f21a83";
    public final static String appsecret = "a75d336caee139a8c243bb36490cc1a5";

    public final static String token = "2018ceshi";

    public final static String tokenKey = "accessToken";

    private static final Map<String, String> keyWords = new HashMap<String, String>();
    private static final Map<String, String> redirectUrls = new HashMap<String, String>();
    private static final Map<String, String> redUrls = new HashMap<String, String>();


    public final static String weiChatUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + ConstantParam.appid + "&secret=" + ConstantParam.appsecret;
    public final static String getCodeUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + ConstantParam.appid + "&secret=" + ConstantParam.appsecret;

    static {
        keyWords.put("tencent", "<a href='" + ConstantParam.findRedUrl("tencent") + "'>点击进入</a>");
        //关键字
        keyWords.put("subscribe", " Hi~ {USERNAME}，谢谢你长得这么好看还关注我\uD83D\uDE0A \n" +
                "-------------------------\n" +
                "-------------------------\n" +
                "" +
                "1.<a href='" + ConstantParam.findRedUrl("qxllqy") + "'>帅哥☞看这里</a>\n" +
                "2.<a href='" + ConstantParam.findRedUrl("llfudai") + "'>美女☞看这里</a>\n" +
                "3.<a href='" + ConstantParam.findRedUrl("cydx") + "'>逗比☞看这里</a>\n" +
                "4.<a href='" + ConstantParam.findRedUrl("gdym") + "'>更多活动\uD83D\uDC11☞看这里</a>\n" +
                "-------------------------\n" +
                "更多精彩请在下方输入“我喜欢你”,有惊喜哦！\n");
        keyWords.put("else", " " +
                "您好呀，很高兴和你成为朋友~沃特权圈圈随时为您服务，您可在对话框内输入以下数字进行解答\n" +
                "【1】你是个帅哥\n" +
                "【2】你是个美女\n" +
                "【3】你是个逗比\n" +
                "【4】你是个傻逼\n" +
                "【5】你是个二货\n" +
                "【6】你什么都不是");

        keyWords.put("1", "帅哥, 你好！");
        keyWords.put("2", "美女, 你好！");
        keyWords.put("3", "逗比, 你好！");
        keyWords.put("4", "傻逼, 你好！");
        keyWords.put("5", "二货, 你好！");
        keyWords.put("5", "什么都不是, 你好！");
    }


    public static String findRedirectUrl(String key) {
        String res = redirectUrls.get(key);
        if (null == res || "".equals(res)) {
            res = redirectUrls.get("991");
        }
        return res;
    }

    public static String findResponseText(String key) {
        String res = keyWords.get(key);
        if (null == res || "".equals(res)) {
            res = keyWords.get("else");
        }
        return res;
    }

    public static String findRedUrl(String key) {

        String res = redUrls.get(key);
        if (null == res || "".equals(res)) {
            res = redUrls.get("else");
        }
        return res;
    }
}
