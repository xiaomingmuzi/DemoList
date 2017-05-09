package com.lixm.chat.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;

import com.lixm.chat.R;

import org.xutils.common.util.LogUtils;

import java.io.File;
import java.io.FileOutputStream;

import io.rong.message.ImageMessage;


/**
 * User: LXM
 * Date: 2015-10-29
 * Time: 15:42
 * Detail:根据文件路径，创建原始图片和缩略图图片
 */
public class UploadImgUtil {
//    10-30 16:33:07.084: I/CnfolCms(12859): com.cnfol.cms.network.GoldExpertAddLiveRequest专家发直播的发送URL-->http://120.24.58.73/gold/livemsgAdd.html
//    10-30 16:29:21.629: I/CnfolCms(12859): com.cnfol.cms.network.GoldExpertLiveRequest发送URL-->http://120.24.58.73/gold/expertMsgInfoNew.html?offsettype=1&groupId=345243&userId=345243&version=1.0&offsetid=0&key=1KFBSbjQWS=0

    public static ImageMessage getImg(Context context) {
        ImageMessage imageMessage = null;
        File imgFileSource=new File(context.getCacheDir(),"source.jpg");
        File imgFileThumb=new File(context.getCacheDir(),"thumb.jpg");
        try {
            //保存原图
            Bitmap bmpSource = BitmapFactory.decodeResource(context.getResources(), R.mipmap.aa);
            imgFileSource.createNewFile();
            FileOutputStream fos=new FileOutputStream(imgFileSource);
            bmpSource.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            //创建缩略图变换矩阵
            Matrix matrix=new Matrix();
            matrix.setRectToRect(new RectF(0, 0, bmpSource.getWidth(), bmpSource.getHeight()), new RectF(0, 0, 160, 160), Matrix.ScaleToFit.CENTER);
            //生成缩略图
            Bitmap bmpThumb=Bitmap.createBitmap(bmpSource,0,0,bmpSource.getWidth(),bmpSource.getHeight(),matrix,true);
            imgFileThumb.createNewFile();
            FileOutputStream fileOutputStream=new FileOutputStream(imgFileThumb);
            bmpThumb.compress(Bitmap.CompressFormat.JPEG, 60, fileOutputStream);
            LogUtils.i("UploadImgUtil--->" + Uri.fromFile(imgFileThumb) + "-----------" + Uri.fromFile(imgFileSource));
            imageMessage=ImageMessage.obtain(Uri.fromFile(imgFileThumb),Uri.fromFile(imgFileSource));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageMessage;
    }

}
