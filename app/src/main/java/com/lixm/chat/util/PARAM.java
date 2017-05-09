package com.lixm.chat.util;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.lixm.chat.application.APP;

import org.xutils.common.util.LogUtils;

import java.security.MessageDigest;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * User: LXM
 * Date: 2015-11-02
 * Time: 14:19
 * Detail:全局参数
 */
public class PARAM {
    public static final int TXT=1;
    public static final int IMG=2;
    public static final int TXTANDIMG=3;

    /*
	 * MD5加密
	 */
    public static String MD5(String s) {
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str).toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * SD卡下语音目录
     */
    public static final String IMAGE_SDCARD_MADER = Environment
            .getExternalStorageDirectory()
            + "/com.cnfol.financialplanner/recorder/";


    /**
     * 建立与融云服务器的连接
     * blog 348392 http://head.cnfolimg.com/7a/67/348392/head.348392.96   kWilRo1NjQ/pUg9zXWrecMktTNuNHS1Zkx2bx0zt4HhoPPT8F61vnYPPueHijQyPdrFGBx5FVdACz1PgMCoxog==
     * <p>
     * guren 347092  http://head.cnfolimg.com/3a/e0/347092/head.347092.96  QBToKar/571CwaPdE4yZlk762pC/b3PFlzYazjzFb0WNfl6jGXiHCItM88q8Sul6Sc/BIV/ptkcee0n1WzQRaw==
     *
     * @param token
     * @param context
     */
    public static void connect(final Context context,String token) {

        if (context.getApplicationInfo().packageName.equals(APP.getCurProcessName(context.getApplicationContext()))) {

            /**
             * IMKit SDK调用第二步,建立与服务器的连接
             */
            RongIM.connect(token, new RongIMClient.ConnectCallback() {

                /**
                 * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
                 */
                @Override
                public void onTokenIncorrect() {
                    LogUtils.i("--onTokenIncorrect");
                }

                /**
                 * 连接融云成功
                 * @param userid 当前 token
                 */
                @Override
                public void onSuccess(String userid) {
                    LogUtils.i("--onSuccess--" + userid);
                    Toast.makeText(context, "融云连接成功！", Toast.LENGTH_SHORT).show();
                }

                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    LogUtils.i("--onError--" + errorCode);
                    Toast.makeText(context, "融云连接失败！" + errorCode, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * 获取屏幕尺寸
     *
     * @return 屏幕尺寸像素值，下标为0的值为宽，下标为1的值为高
     */
    public static int[] getScreenSize(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return new int[]{metrics.widthPixels, metrics.heightPixels};
    }


}
