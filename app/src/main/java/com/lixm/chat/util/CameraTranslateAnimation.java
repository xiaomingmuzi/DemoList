package com.lixm.chat.util;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * User: LXM
 * Date: 2016-09-19
 * Time: 15:39
 * Detail:
 */
public class CameraTranslateAnimation extends Animation {

    private float mFromXValue = 0.0f;
    private float mToXValue = 0.0f;

    private float mFromYValue = 0.0f;
    private float mToYValue = 0.0f;

    private float centerX, centerY;
    private Camera camera;

    private float mFromDegrees = 0;
    private float mToDegrees = 360;
    private float mDepthZ = 10;
    private boolean mReverse = true;

    public CameraTranslateAnimation(float fromXValue, float toXValue,
                                    float fromYValue, float toYValue, float centerX, float centerY) {
        this.mFromXValue = fromXValue;
        this.mToXValue = toXValue;
        this.mFromYValue = fromYValue;
        this.mToYValue = toYValue;
        this.centerX = centerX;
        this.centerY = centerY;
    }

    @Override
    public void initialize(int width, int height, int parentWidth,
                           int parentHeight) {
        // TODO Auto-generated method stub
        super.initialize(width, height, parentWidth, parentHeight);
        camera = new Camera();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        /**  平移  */
        //        float dx = (mFromXValue + (mToXValue - mFromXValue) * interpolatedTime);
        //        float dy = (mFromYValue + (mToYValue - mFromYValue) * interpolatedTime);
        //
        //        Matrix m = t.getMatrix();
        //        camera.save();
        //        camera.translate(dx, dy, 0); // 给要移动的坐标传值
        //        camera.getMatrix(m);
        //        camera.restore();
        //        m.preTranslate(-centerX, -centerY);
        //        m.postTranslate(centerX, centerY); // 回到中心点


        float fromDegrees = mFromDegrees;
        float degrees = fromDegrees
                + ((mToDegrees - fromDegrees) * interpolatedTime); // 每次动画的小差值角度
        Matrix matrix = t.getMatrix(); // 得到Transformation中的所有动画信息矩阵(都存放在矩阵中)
        camera.save();
        if (mReverse) {
            camera.translate(0.0f, 0.0f, -mDepthZ * interpolatedTime); // 在不同轴上的平移效果,通过移动Camear实现
        } else {
            camera.translate(0.0f, 0.0f, mDepthZ * (1.0f - interpolatedTime));
        }
        camera.rotateY(degrees); // 将角度变化用在Y轴上, 用在Z轴上的旋转是,则是普通的旋转
        camera.getMatrix(matrix); // 必须将此矩阵信息叠加到动画的矩阵中,否则在Camera上的设置都不起作用
        camera.restore();
        matrix.preTranslate(-centerX, -centerY); //回到中心点
        matrix.postTranslate(centerX, centerY);
    }
}