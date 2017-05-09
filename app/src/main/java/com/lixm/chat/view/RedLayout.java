package com.lixm.chat.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.lixm.chat.R;
import com.lixm.chat.util.SendMsg;
import com.lixm.chat.util.UIUtils;

import org.xutils.common.util.LogUtils;


/**
 * User: LXM
 * Date: 2016-12-27
 * Time: 15:52
 * Detail:天降红包布局
 */
public class RedLayout extends RelativeLayout implements SendMsg {

    private Context mContext;
    private int mWidth;
    private int marginStart = 0;
    private boolean isMoving = false;
    private Bitmap heart;
    private BitmapDrawable bitmapDrawable;
    private SendMsg actMsg;


    public RedLayout(Context context) {
        super(context);
        initView();
    }

    public RedLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public RedLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        heart = BitmapFactory.decodeResource(getResources(), R.mipmap.red_packe);
        bitmapDrawable = new BitmapDrawable(getResources(), heart);
    }

    public void addRedView(Context context, SendMsg sendMsg) {
        //        LogUtils.e("==========addRedView1===" + System.currentTimeMillis());
        mContext = context;
        actMsg = sendMsg;
        //        LogUtils.d("==========addRedView2===" + System.currentTimeMillis());
        for (int i = 0; i < 15; i++) {
            isMoving = true;
            LogUtils.e("当前的子View=====" + getChildCount());
            RedView redView = addFavor(this);
            addView(redView);
            marginStart = (int) (Math.random() * UIUtils.getScreenSize((Activity) mContext)[0]);
        }
        isMoving = false;
        //        LogUtils.e("==========addRedView3===" + System.currentTimeMillis());
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取本身的宽高 这里要注意,测量之后才有宽高
        mWidth = getMeasuredWidth();

    }

    public void clearAnimation() {
        if (getChildCount() > 0) {
            LogUtils.e("==========getChildCount：" + getChildCount());
            for (int i = 0; i < getChildCount(); i++) {
                getChildAt(i).clearAnimation();
            }
            removeAllViews();
        }
    }


    public RedView addFavor(SendMsg sendMsg) {

        //        LogUtils.w("==========addFavor1===" + System.currentTimeMillis());
        RedView redView = new RedView(getContext());
        setDrawable(redView);
        LayoutParams params = new LayoutParams(UIUtils.dip2px(50), UIUtils.dip2px(120));
        int random = (int) (Math.random() * UIUtils.dip2px(150));
        if (random % 2 == 0)
            params.setMargins(marginStart, -random, 0, 0);
        else
            params.setMargins(marginStart, random, 0, 0);
        redView.setLayoutParams(params);
        redView.startAnimation(sendMsg);
        //        LogUtils.d("==========addFavor2===" + System.currentTimeMillis());
        params = null;
        return redView;
    }

    private void setDrawable(RedView redView) {
        try {
            redView.setImageDrawable(bitmapDrawable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int count = 0;

    @Override
    public void animationEnd() {
        count++;
        if (count % 15 == 0)
            actMsg.animationEnd();
    }
}
