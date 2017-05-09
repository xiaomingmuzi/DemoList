package com.lixm.chat.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lixm.chat.R;
import com.lixm.chat.application.APP;
import com.lixm.chat.bean.UserInfo;
import com.lixm.chat.util.PARAM;
import com.lixm.chat.util.UrlUtils;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtils;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;


public class MainActivity extends Activity {

    private static final String APP_ID = "wx67a6b4b60698ab7c";//微信的APPID
    private static final String APP_SECRET = "9455e320c354a1cf9c39063b1203a7b8";//微信
    public static IWXAPI api;//第三方APP和微信通信的openApi接口

    @ViewInject(R.id.login)
    private Button login;
    @ViewInject(R.id.password)
    private EditText passWord;
    @ViewInject(R.id.userName)
    private EditText name;
    @ViewInject(R.id.textView)
    private TextView textView;
    @ViewInject(R.id.wx_login)
    private Button wxLogin;
    @ViewInject(R.id.loadLayout)
    private LinearLayout loadLayout;
    @ViewInject(R.id.textView3)
    private TextView loadTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        x.view().inject(this);

        //1.注册获取实例
        api = WXAPIFactory.createWXAPI(this, APP_ID, true);//获取IWXAPI的实例
        api.registerApp(APP_ID);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                netForLogin(UrlUtils.LoginUrl, name.getText().toString().trim(), passWord.getText().toString().trim());
            }
        });
        wxLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //2.获取CODE
                //    boolean sendReq(BaseReq req);
                //    sendReq是第三方app主动发送消息给微信，发送完成之后会切回到第三方app界面。
                //    boolean sendResp(BaseResp resp);
                //    sendResp是微信向第三方app请求数据，第三方app回应数据之后会切回到微信界面。
                final SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
                req.state = "wechat_sdk_demo";
                api.sendReq(req);

                //3.在WXEntryActivity中，获取授权返回code值

            }
        });
        showHttp();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!APP.WX_CODE.equals("")) {
            netForWX(APP.WX_CODE);
        }
    }

    /**
     * 通过code,请求微信token和openid和unionid参数
     *
     * @param code
     */
    private void netForWX(final String code) {
        loadLayout.setVisibility(View.VISIBLE);
        loadTxt.append("\n通过code,请求微信token和openid和unionid参数");
//        ?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
        RequestParams params = new RequestParams(UrlUtils.WxToken);
        params.addBodyParameter("appid", APP_ID);
        params.addBodyParameter("secret", APP_SECRET);
        params.addBodyParameter("code", code);
        params.addBodyParameter("grant_type", "authorization_code");
//        https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx67a6b4b60698ab7c&secret=9455e320c354a1cf9c39063b1203a7b8&code=01396176c1f69a654ec23819cc24377C&grant_type=authorization_code
        passWord.setText(UrlUtils.WxToken + "?appid=" + APP_ID + "&secret=" + APP_SECRET + "&code=" + code + "&grant_type=authorization_code");
        loadTxt.append("微信获取Token值--》" + UrlUtils.WxToken + "?appid=" + APP_ID + "&secret=" + APP_SECRET + "&code=" + code + "&grant_type=authorization_code");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.i("微信Token值返回--》" + result);
//                Toast.makeText(MainActivity.this,"获取微信Token成功-->"+ responseInfo.result, Toast.LENGTH_SHORT).show();
                loadTxt.append("\n获取微信Token成功-->" +result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String access_token = jsonObject.optString("access_token");
                    String openid = jsonObject.optString("openid");
                    String unionid = jsonObject.optString("unionid");
                    if (access_token != null && !access_token.equals("")) {
                        netForUserCenter(openid, access_token, code, unionid);
                    } else {
                        netForRefreshToken(APP_ID, jsonObject.optString("refresh_token"));
                        loadTxt.append("融云获取Token失败---》" + jsonObject.optString("errmsg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(MainActivity.this, "获取微信Token值失败!", Toast.LENGTH_SHORT).show();
                loadLayout.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void netForRefreshToken(String appid, String token) {
//    https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN
        RequestParams params = new RequestParams(UrlUtils.WxRefreshToken);
        params.addBodyParameter("appid", appid);
        params.addBodyParameter("grant_type", "refresh_token");
        params.addBodyParameter("refresh_token", token);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.i("刷新Token===》"+result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    /**
     * 登录用户中心
     *
     * @param openId
     * @param token
     * @param code
     * @param unionid
     */
    private void netForUserCenter(String openId, String token, String code, String unionid) {
        loadTxt.append("\n登录用户中心");
        RequestParams params = new RequestParams(UrlUtils.LoginUser);
        params.addBodyParameter("openid", openId);
        params.addBodyParameter("original", "10");
        params.addBodyParameter("token", token);
        params.addBodyParameter("code", code);
        long tid = System.currentTimeMillis() / 1000;
        String time = tid + "";
        String keystr = PARAM.MD5(openId
                + PARAM.MD5(time.substring(time.length() - 6, time.length()))
                + PARAM.MD5(unionid) + "10");
        params.addBodyParameter("keystr", keystr);
        params.addBodyParameter("tid", tid + "");
        params.addBodyParameter("unionid", unionid);
        LogUtils.i("登录用户中心请求接口--》》" + UrlUtils.LoginUser + "?openid=" + openId + "&original=10&token=" + token + "&code=" + code + "&keystr=" + keystr + "&tid=" + tid + "&unionid=" + unionid);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.i("登录用户中心返回数据--》" + result);
                loadTxt.append("\n登录用户中心返回数据==》" + result);
                try {
                    JSONObject object = new JSONObject(result);
                    String flag = object.optString("flag");
                    String msg = object.optString("msg");
                    if (flag.equals("00")) {
                        JSONObject data = object.getJSONObject("info");
                        String userId = data.getString("userid");
                        String nickName = data.getString("nickname");
                        String account = data.getString("username");

                        UserInfo userInfo = new UserInfo();
                        userInfo.setUserId(userId);
                        userInfo.setNickName(nickName);
                        userInfo.setUserAccount(account);
                        if (userInfo != null) {
                            LogUtils.i(userInfo.toString());
                            loadTxt.append(userInfo.toString());
                            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                            intent.putExtra("UserInfo", userInfo);
                            startActivity(intent);
                        }

                    } else {
                        loadTxt.append("获取用户中心失败!");
                        Toast.makeText(MainActivity.this, "获取用户中心失败!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    LogUtils.i("<---" + getClass().getName() + "返回json格式错误--->"
                            + e.getMessage());
                    loadTxt.append("\n获取用户中心失败!");
                    Toast.makeText(MainActivity.this, "用户中心数据解析异常", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                loadTxt.append("\n获取用户中心失败!");
                Toast.makeText(MainActivity.this, "获取用户中心失败!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 登录用户中心
     *
     * @param url
     * @param userName
     * @param passWord
     */
    private void netForLogin(String url, String userName, String passWord) {

        RequestParams params = new RequestParams(url);
        params.addQueryStringParameter("platform", "1");
        params.addQueryStringParameter("channel", "1");
        params.addQueryStringParameter("act", "login");
        params.addQueryStringParameter("username", userName);
        params.addQueryStringParameter("password", passWord);
        LogUtils.i(url + "?platform=1&channel=1&act=login&username=" + userName + "&password=" + passWord);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.i("接口返回数据==》》" + result);
                UserInfo userInfo = parseData(result);
                if (userInfo != null) {
                    Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                    intent.putExtra("UserInfo", userInfo);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "数据解析异常!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(MainActivity.this, "登录失败!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 解析数据
     *
     * @param msg
     */
    private UserInfo parseData(String msg) {
        UserInfo userInfo = null;
        try {
            JSONObject object = new JSONObject(msg);
            String str = object.getString("flag");
            if ("111".equals(str)) {
                JSONObject data = object.optJSONObject("data");
                String userId = data.optString("userid");
                String nickName = data.optString("nickname");
                String account = data.optString("username");
                String money = data.optString("money");
                String mobile = data.optString("mobile");

                userInfo = new UserInfo();
                userInfo.setUserId(userId);
                userInfo.setNickName(nickName);
                userInfo.setUserAccount(account);
                userInfo.setGolden(money);
                userInfo.setMobile(mobile);
                LogUtils.i(userInfo.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userInfo;
    }

    private void showHttp() {
        String content = "地方<span style=\"color:#E53333;\">[/笑脸]大发生的<strong> 阿斯顿发生<span style=\"color:#337FE5;\">上发生地方</span></strong></span>";
        String con = "我的笑脸，哈哈哈";
        SpannableString spannableString = new SpannableString(con);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#333333")), 2, 4, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#e53333;")), 2, 4, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {

            }
        }, 1, 3, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.setText(Html.fromHtml(content));
        textView.append(spannableString);
        textView.setVisibility(View.VISIBLE);
    }

}
