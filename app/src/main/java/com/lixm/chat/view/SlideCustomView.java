package com.lixm.chat.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lixm.chat.R;

import org.xutils.common.util.LogUtils;

/**
 * User: LXM
 * Date: 2016-08-25
 * Time: 11:20
 * Detail:自定义可滑动的view
 */
public class SlideCustomView extends RelativeLayout implements View.OnTouchListener {

    private Context mContext;
    private boolean bCleanMode = false;
    private float mOldX;//用户点击的横坐标
    private VelocityTracker mVelocityTracker;//滑动速度
    private static final int XSPEED_MIN = 200;    //手指向下滑动时的最小速度
    private static final float XDISTANCE_MIN = 150;    //手指向下滑动时的最小距离
    private View contentView;//要显示滑动的布局
    private TranslateAnimation translateAnimation1;
    private TranslateAnimation translateAnimation2;

    public SlideCustomView(Context context) {
        super(context);
        mContext=context;
    }

    public SlideCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
    }

    public SlideCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext=context;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    public void setContentView(View content) {
        setOnTouchListener(this);
        contentView = content;
        translateAnimation1=new TranslateAnimation(0,720,0,0);
        translateAnimation1.setFillAfter(true);
        translateAnimation1.setRepeatCount(0);
        translateAnimation1.setDuration(300);
        translateAnimation2=new TranslateAnimation(720, 0,0,0);
        translateAnimation2.setFillAfter(true);
        translateAnimation2.setRepeatCount(0);
        translateAnimation2.setDuration(300);
        LogUtils.e("==============setContentView================"+getWidth());
        ImageView imageView=new ImageView(mContext);
        imageView.setBackgroundResource(R.mipmap.bg);
        LayoutParams params=new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(params);
        addView(imageView);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        createVelocityTracker(event);
        LogUtils.e("==============onTouch================"+event.getAction());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mOldX = event.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                //获取顺时速度
                int xSpeed = getScrollVelocity();
                //当滑动的距离大于我们设定的最小距离且滑动的瞬间速度大于我们设定的速度时，显示无障碍界面
                if (event.getRawX() - mOldX > XDISTANCE_MIN && !bCleanMode && xSpeed > XSPEED_MIN) {
                    bCleanMode = true;
                    this.startAnimation(translateAnimation1);
                } else if (mOldX - event.getRawX() > XDISTANCE_MIN && bCleanMode && xSpeed > XSPEED_MIN) {
                    bCleanMode = false;
                    this.startAnimation(translateAnimation2);
                }
//                contentView.layout((int) (event.getRawX() - mOldX), contentView.getTop(), (int) (contentView.getRight() + event.getRawX() - mOldX), contentView.getBottom());
                LogUtils.e("==============contentView.layout================");
                break;
            case MotionEvent.ACTION_UP:
                recycleVelocityTracker();
                break;
        }
        return true;
    }

    /**
     * 通过layout方法，移动view
     * 优点：对view所在的布局，要求不苛刻，不要是RelativeLayout，而且可以修改view的大小
     *
     * @param view
     * @param rawX
     * @param rawY
     */
    private void moveViewByLayout(View view, int rawX, int rawY) {
        int left = rawX - getWidth() / 2;
        int top = rawY - getHeight() / 2;
        int width = left + view.getWidth();
        int height = top + view.getHeight();
        view.layout(left, top, width, height);
    }

    /**
     * 创建VelocityTracker对象，并将触摸content界面的滑动事件加入到VelocityTracker当中。
     */
    private void createVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    /**
     * 回收VelocityTracker对象。
     */
    private void recycleVelocityTracker() {
        mVelocityTracker.recycle();
        mVelocityTracker = null;
    }

    /**
     * 获取手指在content界面滑动的速度。
     *
     * @return 滑动速度，以每秒钟移动了多少像素值为单位。
     */
    private int getScrollVelocity() {
        mVelocityTracker.computeCurrentVelocity(1000);
        int velocity = (int) mVelocityTracker.getXVelocity();
        return Math.abs(velocity);
    }

}
