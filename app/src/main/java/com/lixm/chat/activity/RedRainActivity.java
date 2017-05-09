package com.lixm.chat.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.lixm.chat.R;
import com.lixm.chat.util.SendMsg;
import com.lixm.chat.view.RedLayout;

import org.xutils.common.util.LogUtils;

public class RedRainActivity extends Activity implements SendMsg {

    private RedLayout redLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red_rain);
        redLayout = (RedLayout) findViewById(R.id.red_layout);
        redLayout.addRedView(this, this);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    redLayout.addRedView(RedRainActivity.this, RedRainActivity.this);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void animationEnd() {
        redLayout.clearAnimation();
        LogUtils.e("========动画结束====="+redLayout.getChildCount());
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                            handler.sendEmptyMessage(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        super.run();
                    }
                }.start();
    }

}
