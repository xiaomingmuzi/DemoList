package com.lixm.chat.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.widget.ImageView;

import com.lixm.chat.R;

public class FourActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four);
        //        SlideCustomView slideCustomView = (SlideCustomView) findViewById(R.id.root);
        //        ImageView img = (ImageView) findViewById(R.id.img);
        //        slideCustomView.setContentView(img);
        ImageView img = (ImageView) findViewById(R.id.img);
        img.setImageBitmap(watermarkBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.bg), BitmapFactory.decodeResource(getResources(), R.mipmap.bottom), "中金财经"));
    }

    /**
     * 图片添加水印和文字
     *
     * @param src
     * @param watermark
     * @param title
     * @return
     */
    public static Bitmap watermarkBitmap(Bitmap src, Bitmap watermark,
                                         String title) {
        if (src == null) {
            return null;
        }
        int w = src.getWidth();
        int h = src.getHeight();
        //需要处理图片太大造成的内存超过的问题,这里我的图片很小所以不写相应代码了
        Bitmap newb = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
        Canvas cv = new Canvas(newb);
        cv.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入src
        float fontWidth = 0f;
        float fontHeight=0f;
        //加入文字
        //        if (title != null) {
        String familyName = "宋体";
        Typeface font = Typeface.create(familyName, Typeface.BOLD);
        TextPaint textPaint = new TextPaint();
        textPaint.setColor(Color.RED);
        textPaint.setTypeface(font);
        textPaint.setTextSize(34);
        fontWidth = textPaint.measureText(title);
        Rect rect = new Rect();

        //返回包围整个字符串的最小的一个Rect区域
        textPaint.getTextBounds(title, 0, 1, rect);

//        strwid = rect.width();
        fontHeight = rect.height();
        //这里是自动换行的
        //            StaticLayout layout = new StaticLayout(title, textPaint, w, Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
        //            layout.draw(cv);
        //文字就加左上角算了
//        cv.drawText(title, 0, 40, textPaint);
        //        }

        Paint paint = new Paint();
        //加入图片
        //        if (watermark != null) {
        int ww = watermark.getWidth();
        int wh = watermark.getHeight();
        paint.setAlpha(90);
        Log.i("FourActivity","======文字宽度==》"+fontWidth+"====文字高度=="+fontHeight+"===原图片高度=="+h+"=====原图片宽度==="+w);
        Log.i("FourActivity","======图标宽度==》"+ww+"====图标高度=="+wh);
        cv.drawBitmap(watermark, w - ww - fontWidth - 5, h - wh - 45, paint);// 在src的右下角画入水印
        Log.i("FourActivity","======文字显示的位置==》"+(w - fontWidth)+"  "+ (h - wh - 20+((wh-fontHeight)/2)));
        cv.drawText(title, w - fontWidth, h - wh +((wh-fontHeight)/2)-20, textPaint);
        //        }

        cv.save(Canvas.ALL_SAVE_FLAG);// 保存
        cv.restore();// 存储
        return newb;
    }
}
