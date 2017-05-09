/*
 * Copyright (C) 2015 tyrantgit
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lixm.chat.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.lixm.chat.util.SendMsg;
import com.lixm.chat.util.UIUtils;


/**
 * User: LXM
 * Date: 2016-12-26
 * Time: 19:59
 * Detail:单个红包View
 */
public class RedView extends ImageView implements Rotate3DNumberAnimation.InterpolatedTimeListener {

    private Context context;
    private boolean enableRefresh;
    private int mWidth;
    private int mHeight;
    private SendMsg toLayout;
    private MyAnimotion myAnimotion;

    public RedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        myAnimotion = new MyAnimotion();
    }

    public RedView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        myAnimotion = new MyAnimotion();
    }

    public RedView(Context context) {
        super(context);
        this.context = context;
        myAnimotion = new MyAnimotion();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(UIUtils.dip2px(40), UIUtils.dip2px(70));
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    Rotate3DNumberAnimation rotateAnim = null;
    AnimationSet animationSet = null;
    TranslateAnimation translateAnimation = null;
    RotateAnimation rotateAnimation = null;

    public void startAnimation(SendMsg toLayout) {
        this.toLayout = toLayout;
        animationSet = new AnimationSet(context, null);
        translateAnimation = new TranslateAnimation(0, 0, 0, UIUtils.getScreenSize((Activity) context)[1] + UIUtils.dip2px(150));

        float cX = mWidth / 2.0f;
        float cY = mHeight / 2.0f;
        int random = (int) (Math.random() * 10);
        if (random % 2 == 0) {
            rotateAnim = new Rotate3DNumberAnimation(cX, cY, Rotate3DNumberAnimation.ROTATE_DECREASE);
        } else {
            rotateAnim = new Rotate3DNumberAnimation(cX, cY, Rotate3DNumberAnimation.ROTATE_INCREASE);
        }


        float rand = (float) (Math.random() * 360);
        rotateAnimation = new RotateAnimation(0, rand, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        if (random % 2 == 0)
            animationSet.addAnimation(rotateAnim);
        else
            animationSet.addAnimation(rotateAnimation);

        animationSet.addAnimation(translateAnimation);
        rotateAnim.setInterpolatedTimeListener(RedView.this);
        animationSet.setDuration(3000);
        animationSet.setFillAfter(true);
        animationSet.setInterpolator(new DecelerateInterpolator());
        animationSet.setAnimationListener(myAnimotion);

        RedView.this.startAnimation(animationSet);
    }

    /**
     * dp转换成像素
     *
     * @param dp
     * @return
     */
    public int dp2Px(float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    @Override
    public void interpolatedTime(float interpolatedTime) {
        // 监听到翻转进度过半时，更新txtNumber显示内容。
        if (enableRefresh && interpolatedTime > 0.5f) {
            enableRefresh = false;
        }
        //改变透明度
        if (interpolatedTime > 0.5f) {
            this.setAlpha((interpolatedTime - 0.5f) * 2);
        } else {
            this.setAlpha(1 - interpolatedTime * 2);
        }
    }


    class MyAnimotion implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            rotateAnim = null;
            animationSet = null;
            translateAnimation = null;
            rotateAnimation = null;
            RedView.this.setVisibility(GONE);
            toLayout.animationEnd();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

}
