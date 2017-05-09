package com.lixm.chat.view;

/**
 * User: LXM
 * Date: 2016-05-10
 * Time: 09:43
 * Detail:保存视频截图线程类
 */
import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class ViewThread extends Thread {
    private CustomSurfaceView mCostomSurfaceView;
    private SurfaceHolder mHolder;
    private boolean mRun = false;
    private long mStartTime;
    private long mElapsed;

    public ViewThread(CustomSurfaceView CostomSurfaceView) {
        mCostomSurfaceView = CostomSurfaceView;
        mHolder = mCostomSurfaceView.getHolder();
    }

    public void setRunning(boolean run) {
        mRun = run;
    }

    @Override
    public void run() {
        Canvas canvas = null;
        mStartTime = System.currentTimeMillis();
        while (mRun) {
            canvas = mHolder.lockCanvas();
            if (canvas != null) {
                mCostomSurfaceView.doDraw(mElapsed, canvas);
                mElapsed = System.currentTimeMillis() - mStartTime;
                mHolder.unlockCanvasAndPost(canvas);
            }
            mStartTime = System.currentTimeMillis();
        }
    }
}
