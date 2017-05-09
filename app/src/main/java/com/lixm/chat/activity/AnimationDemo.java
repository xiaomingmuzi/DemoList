package com.lixm.chat.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

import com.lixm.chat.R;
import com.lixm.chat.util.CameraTranslateAnimation;

/**
 * User: LXM
 * Date: 2016-09-19
 * Time: 15:38
 * Detail:
 */
public class AnimationDemo extends Activity {
    private ImageView img;
    private CameraTranslateAnimation cameraTranslateAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_demo);
        WindowManager windowManager= (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display=  windowManager.getDefaultDisplay();
        img = (ImageView) findViewById(R.id.imageView);
        cameraTranslateAnimation = new CameraTranslateAnimation(0,display.getWidth(), 0, 0, display.getWidth()/2, display.getWidth()/2);
        cameraTranslateAnimation.setDuration(3000);
        cameraTranslateAnimation.setFillAfter(true);
        cameraTranslateAnimation.setRepeatCount(-1);
        img.setAnimation(cameraTranslateAnimation);
        img.startAnimation(cameraTranslateAnimation);
    }
}
