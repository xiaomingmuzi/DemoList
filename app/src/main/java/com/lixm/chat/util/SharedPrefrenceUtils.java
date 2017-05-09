package com.lixm.chat.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.lixm.chat.bean.UserInfo;

/**
 * User: LXM
 * Date: 2016-03-10
 * Time: 10:49
 * Detail:SP工具类
 */
public class SharedPrefrenceUtils {
    private Context mContext;
    private SharedPreferences mSp;

    public SharedPrefrenceUtils(Context context) {
        this.mContext = context;
    }

    /**
     * 保存用户信息
     * userId;
     * private String nickName;
     * private String isHead;
     * private String picUrl;//用户头像
     * private String userAccount;// 账号
     * private String tempMinAttentionNews;
     * private String issuePrimaryId;
     * private String golden;// 金币数
     * private String checkcode;//后踢前码
     * private String password;// 用户账户的密码
     * private String mobile;//手机号
     * private String kt;// 博客界面请求是否开通博客参数
     * private String memberId;// 用户的memberId
     * private String token;//融云Token值
     *
     * @param userInfo
     */
    public void saveUserInfo(UserInfo userInfo) {
        mSp = mContext.getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        mSp.edit().putString("userId", userInfo.getUserId()).apply();
        mSp.edit().putString("nickName", userInfo.getNickName()).apply();
        mSp.edit().putString("picUrl", userInfo.getPicUrl()).apply();
        mSp.edit().putString("userAccount", userInfo.getUserAccount()).apply();
        mSp.edit().putString("golden", userInfo.getGolden()).apply();
        mSp.edit().putString("password", userInfo.getPassword()).apply();
        mSp.edit().putString("mobile", userInfo.getMobile()).apply();
        mSp.edit().putString("token", userInfo.getToken()).apply();
    }

    /**
     * 获取用户信息
     * @return
     */
    public UserInfo getUserInfo() {
        mSp = mContext.getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        UserInfo userInfo = null;
        if (!TextUtils.isEmpty(mSp.getString("userId", null))) {
            userInfo = new UserInfo();
            userInfo.setUserId(mSp.getString("userId", null));
            userInfo.setNickName(mSp.getString("nickName", null));
            userInfo.setPicUrl(mSp.getString("picUrl", null));
            userInfo.setUserAccount(mSp.getString("userAccount", null));
            userInfo.setGolden(mSp.getString("golden", null));
            userInfo.setPassword(mSp.getString("password", null));
            userInfo.setMobile(mSp.getString("mobile", null));
            userInfo.setToken(mSp.getString("token", null));
        }
        return userInfo;
    }

    /**
     * 删除用户信息
     */
    public void deleteUserInfo() {
        mSp.edit().clear().commit();
    }

}
