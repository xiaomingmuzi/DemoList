package com.lixm.chat.application;

import android.app.Application;


import android.app.ActivityManager;
import android.content.Context;
import android.os.Handler;

import com.lixm.chat.database.DBHelper;

import org.xutils.x;

import io.rong.imkit.RongIM;

/**
 * User: LXM
 * Date: 2015-10-26
 * Time: 17:39
 * Detail:
 */
public class APP extends Application {

    private static APP mContext;
    public static String WX_CODE = "";//微信授权成功后，返回的code�?
    private boolean flag=true;//日志输出
    // 获取到主线程的handler
    private static Handler mMainThreadHandler = null;
    // 获取到主线程
    private static Thread mMainThread = null;
    // 获取到主线程的id
    private static int mMainThreadId;

    @Override
    public void onCreate() {
        super.onCreate();
        /**
         * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使�? RongIM 的进程和 Push 进程执行�? init�?
         * io.rong.push 为融�? push 进程名称，不可修改�??
         */
        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext())) ||
                "io.rong.push".equals(getCurProcessName(getApplicationContext()))) {

            /**
             * IMKit SDK调用第一�? 初始�?
             */
            RongIM.init(this);
        }
        x.Ext.init(this);
        x.Ext.setDebug(flag);
        DBHelper.init((APP) getApplicationContext());
        mContext=this;
    }

    /**
     * 获得当前进程的名�?
     *
     * @param context
     * @return 进程�?
     */
    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    // 对外暴露上下文
    public static APP getApplication() {
        return mContext;
    }
    // 对外暴露主线程id
    public static int getMainThreadId() {
        return mMainThreadId;
    }
}
