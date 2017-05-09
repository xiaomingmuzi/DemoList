package com.lixm.chat.activity;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.lixm.chat.R;

public class LiveAnimationActivity extends Activity {
    private ImageView img;
    private Button btn;
    private AnimationDrawable animationDrawable;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (animationDrawable.isRunning())
                        animationDrawable.stop();
                    img.setVisibility(View.GONE);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_animation);
        img = (ImageView) findViewById(R.id.img);
        btn= (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img.setImageResource(R.drawable.live_gold_brick);
                animationDrawable = (AnimationDrawable) img.getDrawable();
                animationDrawable.start();
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        handler.sendEmptyMessage(1);
                    }
                }.start();
            }
        });

    }
}
