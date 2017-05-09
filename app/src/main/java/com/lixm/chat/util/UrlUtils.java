package com.lixm.chat.util;

/**
 * User: LXM
 * Date: 2015-10-27
 * Time: 16:06
 * Detail:地址接口
 */
public class UrlUtils {
    public static String LoginUrl="http://passport.test.api.3g.cnfol.com/index.php/userlogin/applogin";//登录接口
    public static String GetToken="http://120.24.58.73/gold/getTokenMessage.html";//获取融云Token   GET
    public static String WxToken="https://api.weixin.qq.com/sns/oauth2/access_token";//获取微信Token
    public static String WxRefreshToken="https://api.weixin.qq.com/sns/oauth2/refresh_token";//刷新微信Token值
    public static String LoginUser="http://passport.test.api.3g.cnfol.com/api/mlogin/getuseroauthinfobywx";//第三方账号登录用户中心
}
