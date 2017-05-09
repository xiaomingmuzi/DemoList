package com.lixm.chat.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * User: LXM
 * Date: 2016-12-22
 * Time: 20:36
 * Detail:
 */
public class BesselView extends View {

    private int mWidth;
    private int mHeight;
    private Paint mPaint;
    private Path mPath;


    public BesselView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BesselView(Context context) {
        super(context);

    }

    public BesselView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(Color.RED);
        mPath = new Path();
        mPath.moveTo(mWidth / 2, 0);
        int width = mWidth / 4 * 3;
        for (int i = 1; i < 30; i++) {
            width -= 50;
            int divider = i % 2 == 0 ? -width / 2 : width / 2;
            mPath.quadTo(mWidth / 2 + divider, (i - 1) * 100 + 50, mWidth / 2, i * 100);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }
}
