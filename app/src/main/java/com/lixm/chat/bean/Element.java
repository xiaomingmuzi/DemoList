package com.lixm.chat.bean;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.lixm.chat.R;

import java.io.BufferedInputStream;
import java.io.Serializable;


/**
 * User: LXM
 * Date: 2016-05-10
 * Time: 09:53
 * Detail:
 */
public class Element implements Serializable{
    private float mX;
    private float mY;

    private Bitmap mBitmap;

    public Element(Resources res, int x, int y) {
        mBitmap = BitmapFactory.decodeStream(new BufferedInputStream(res.openRawResource(R.raw.ic_launcher)));
        mX = x - mBitmap.getWidth() / 2;
        mY = y - mBitmap.getHeight() / 2;
    }

    public void doDraw(Canvas canvas) {
        canvas.drawBitmap(mBitmap, mX, mY, null);
    }
}
