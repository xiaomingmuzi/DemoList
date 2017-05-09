package com.lixm.chat.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import com.lixm.chat.R;
import com.lixm.chat.view.Rotate3DNumberAnimation;

public class SevenActivity extends Activity implements View.OnClickListener, Rotate3DNumberAnimation.InterpolatedTimeListener {

    private Button btnIncrease, btnDecrease;
    private ImageView txtNumber;
    /**
     * TextNumber是否允许显示最新的数字。
     */
    private boolean enableRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seven);
        btnIncrease = (Button) findViewById(R.id.button4);
        btnDecrease = (Button) findViewById(R.id.button5);
        txtNumber = (ImageView) findViewById(R.id.textView4);
        btnIncrease.setOnClickListener(this);
        btnDecrease.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        enableRefresh = true;
        Rotate3DNumberAnimation rotateAnim = null;
        AnimationSet animationSet = new AnimationSet(this, null);
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, 500);
        float cX = txtNumber.getWidth() / 2.0f;
        float cY = txtNumber.getHeight() / 2.0f;
        if (v == btnDecrease) {
            rotateAnim = new Rotate3DNumberAnimation(cX, cY, Rotate3DNumberAnimation.ROTATE_DECREASE);
        } else if (v == btnIncrease) {
            rotateAnim = new Rotate3DNumberAnimation(cX, cY, Rotate3DNumberAnimation.ROTATE_INCREASE);
        }
        if ( rotateAnim != null) {
            animationSet.addAnimation(rotateAnim);
            animationSet.addAnimation(translateAnimation);
            rotateAnim.setInterpolatedTimeListener(this);
            animationSet.setFillAfter(true);
            animationSet.setDuration(1000);

            txtNumber.startAnimation(animationSet);
        }
    }

    @Override
    public void interpolatedTime(float interpolatedTime) {
        // 监听到翻转进度过半时，更新txtNumber显示内容。
        if (enableRefresh && interpolatedTime > 0.5f) {
            enableRefresh = false;
        }
        //改变透明度
        if (interpolatedTime > 0.5f) {
            txtNumber.setAlpha((interpolatedTime - 0.5f) * 2);
        } else {
            txtNumber.setAlpha(1 - interpolatedTime * 2);
        }
    }
}
