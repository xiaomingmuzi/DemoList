package com.lixm.chat.util;

import android.media.MediaRecorder;
import android.os.Handler;
import android.widget.ImageView;

import com.lixm.chat.R;

import org.xutils.common.util.LogUtils;

import java.io.File;

/**
 * 录音工具类
 *
 * @author Administrator
 */
public class MediaRecorderUtils {

    private static MediaRecorder recorder;
    static MediaRecorderUtils mediaRecorderUtils;
    static ImageView mimageView;
    private String path;

    /**
     * 获得单例对象，传入一个显示音量大小的imageview对象，如不需要显示可以传null
     */
    public static MediaRecorderUtils getInstence(ImageView imageView) {
        if (mediaRecorderUtils == null) {
            mediaRecorderUtils = new MediaRecorderUtils();
        }
        mimageView = imageView;
        return mediaRecorderUtils;
    }

    /**
     * 获得音频路径
     */
    public String getPath() {
        return path;
    }

    /**
     * 初始化
     */
    private void init() {

        recorder = new MediaRecorder();// new出MediaRecorder对象
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        // 设置MediaRecorder的音频源为麦克风
        recorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
        // 设置MediaRecorder录制的音频格式
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        // 设置MediaRecorder录制音频的编码为amr.
        File file = new File(PARAM.IMAGE_SDCARD_MADER);
        if (!file.exists()) {
            file.mkdirs();
        }
        path = PARAM.IMAGE_SDCARD_MADER + System.currentTimeMillis() + "stock.amr";
        recorder.setOutputFile(path);
        // 设置录制好的音频文件保存路径
        try {
            recorder.prepare();// 准备录制
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始录音
     */
    public void MediaRecorderStart() {
        init();
        try {
            recorder.start();
            flag = true;
            if (mimageView != null) {
                updateMicStatus();
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e("录制===失败");
        }
    }

    /**
     * 停止录音
     */
    public void MediaRecorderStop() {
        try {
            recorder.stop();
            recorder.release(); //释放资源
            flag = false;
            mimageView = null;
            recorder = null;
        } catch (Exception e) {
            e.toString();
        }

    }

    /**
     * 删除已录制的音频
     */
    public void MediaRecorderDelete() {
        try {
            File file = new File(path);
            if (file.isFile()) {
                file.delete();
            }
            file.exists();
        } catch (Exception e) {
            e.toString();
        }
    }

    /**
     * 控制声音大小的显示效果
     */
    public void setImageType(int type) {
        image_type = type;
    }

    private final Handler mHandler = new Handler();
    private Runnable mUpdateMicStatusTimer = new Runnable() {
        public void run() {
            updateMicStatus();
        }
    };
    private int BASE = 1;
    private int SPACE = 1000;// 间隔取样时间
    private boolean flag = true;
    private int image_type = 0;
    private int[] image0 = new int[]{R.drawable.rc_ic_volume_1, R.drawable.rc_ic_volume_2, R.drawable.rc_ic_volume_3, R.drawable.rc_ic_volume_4, R.drawable.rc_ic_volume_5, R.drawable.rc_ic_volume_6, R.drawable.rc_ic_volume_7, R.drawable.rc_ic_volume_8};
//    private int[] image1 = new int[]{R.mipmap.broadcast_1, R.mipmap.broadcast_2, R.mipmap.broadcast_3, R.mipmap.broadcast_4, R.mipmap.broadcast_5, R.mipmap.broadcast_6, R.mipmap.broadcast_7, R.mipmap.broadcast_8};

    /**
     * 更新话筒状态
     */
    private void updateMicStatus() {
        try {


            if (recorder != null) {
                double ratio = (double) recorder.getMaxAmplitude() / BASE;
                double db = 0;// 分贝
                if (ratio > 1) {
                    db = 20 * Math.log10(ratio);
                }
                int i = (int) db / 10;
                int image_id = 0;
                switch (i) {
                    case 1:
                        image_id = getImage_id(0);
                        break;
                    case 2:
                        image_id = getImage_id(1);
                        break;
                    case 3:
                        image_id = getImage_id(2);
                        break;
                    case 4:
                        image_id = getImage_id(3);
                        break;
                    case 5:
                        image_id = getImage_id(4);
                        break;
                    case 6:
                        image_id = getImage_id(5);
                        break;
                    case 7:
                        image_id = getImage_id(6);
                        break;
                    case 8:
                        image_id = getImage_id(7);
                        break;
                    default:
                        image_id = getImage_id(0);
                        break;
                }
                mimageView.setImageResource(image_id);
                if (flag) {
                    mHandler.postDelayed(mUpdateMicStatusTimer, SPACE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getImage_id(int i) {
//        switch (image_type) {
//            case 0:
                return image0[i];
//            case 1:
//                return image1[i];
//        }
//        return 0;
    }

}
