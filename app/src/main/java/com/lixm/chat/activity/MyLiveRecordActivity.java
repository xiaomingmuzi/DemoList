package com.lixm.chat.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.ksy.recordlib.service.core.KSYStreamer;
import com.ksy.recordlib.service.core.KSYStreamerConfig;
import com.ksy.recordlib.service.streamer.OnStatusListener;
import com.ksy.recordlib.service.streamer.RecorderConstants;
import com.ksy.recordlib.service.util.audio.OnProgressListener;
import com.lixm.chat.R;
import com.lixm.chat.util.KSYKey;

import org.xutils.common.util.LogUtils;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MyLiveRecordActivity extends Activity implements View.OnClickListener {
    //rtmp://***.uplive.ks-cdn.com/app/stream?signature=vU9XqPLcXd3nWdlfLWIhruZrLAM%3D&accesskey=P3UPCMORAFON76Q6RTNQ&expire=1436976000&nonce=4e1f2519c626cbfbab1520c255830c26&public=0＆vdoid=123456
    //    app: RTMP协议中的应用名
    //
    //    stream:RTMP协议中的流名
    //
    //    signature:生成的签名  KSYKey.SECRETKEY
    //
    //    expire:签名的过期时间，Linux UTC标准时间戳，如1141889120，超过此时间的URL将无法使用
    //
    //    public:表示流的公开或私密，默认值为1即为公开，值为0表示私密
    //
    //    nonce:可选随机数
    //
    //    vdoid:可选，用于唯一标识该Stream的每次推流，开启直播转点播时，该值将作为点播文件名的一部分（见直播转点播部分说明）。
    //    private String mUrl = "rtmp://test.uplive.ksyun.com/live/androidtest";
    private String mUrl = "rtmp://test.uplive.ks-cdn.com/live/androidtest?signature=";
    private static final String START_STRING = "开始直播";
    private static final String STOP_STRING = "停止直播";
    private boolean recording = false;//是否开始录制
    private boolean audio_mix = false;//是否混音
    private String mBgmPath = "/sdcard/test.mp3";//混音地址
    private boolean printDebugInfo = false;//打印日志
    private Timer timer;//计时器
    private boolean startAuto = false;//初始化完成后启动推流
    private String mDebugInfo = "";//调试信息
    ExecutorService executorService = Executors.newSingleThreadExecutor();//单例线程，任意时间池中只能有一个线程
    private boolean mAcitivityResumed = false;//Activity是否可见

    //1.初始化播放器
    private GLSurfaceView mCameraPreview;
    //2.初始号KSYStreamerConfig
    private KSYStreamerConfig.Builder builder;
    //3.设置播放
    private KSYStreamer mStreamer;
    private Handler mHandler;
    //4.设置监听器
    private OnStatusListener mOnErrorListener = new OnStatusListener() {
        @Override
        public void onStatus(int what, int arg1, int arg2, String msg) {
            // msg may be null
            switch (what) {
                case RecorderConstants.KSYVIDEO_OPEN_STREAM_SUCC:
                    // 推流成功
                    LogUtils.d(" 推流成功");
                    mHandler.obtainMessage(what, "start stream succ")
                            .sendToTarget();
                    break;
                case RecorderConstants.KSYVIDEO_ENCODED_FRAMES_THRESHOLD:
                    //认证失败且超过编码上限
                    LogUtils.d("认证失败且超过编码上限");
                    mHandler.obtainMessage(what, "KSYVIDEO_ENCODED_FRAME_THRESHOLD")
                            .sendToTarget();
                    break;
                case RecorderConstants.KSYVIDEO_AUTH_FAILED:
                    //认证失败
                    LogUtils.d("认证失败");
                    break;
                case RecorderConstants.KSYVIDEO_ENCODED_FRAMES_FAILED:
                    //编码失败
                    LogUtils.e("---------编码失败");
                    break;
                case RecorderConstants.KSYVIDEO_FRAME_DATA_SEND_SLOW:
                    //网络状况不佳
                    if (mHandler != null) {
                        mHandler.obtainMessage(what, "network not good").sendToTarget();
                    }
                    break;
                case RecorderConstants.KSYVIDEO_EST_BW_DROP:
                case RecorderConstants.KSYVIDEO_EST_BW_RAISE:
                case RecorderConstants.KSYVIDEO_AUDIO_INIT_FAILED:

                    break;
                case RecorderConstants.KSYVIDEO_INIT_DONE:
                    mHandler.obtainMessage(what, "init done")
                            .sendToTarget();
                    break;
                default:
                    if (msg != null) {
                        // 可以在这里处理断网重连的逻辑
                        if (TextUtils.isEmpty(mUrl)) {
                            mStreamer
                                    .updateUrl("rtmp://test.uplive.ksyun.com/live/androidtest");
                        } else {
                            mStreamer.updateUrl(mUrl);
                        }
                        if (!executorService.isShutdown()) {
                            executorService.submit(new Runnable() {

                                @Override
                                public void run() {
                                    boolean needReconnect = true;
                                    try {
                                        while (needReconnect) {
                                            Thread.sleep(3000);
                                            //只在Activity对用户可见时重连
                                            if (mAcitivityResumed) {
                                                if (mStreamer.startStream()) {
                                                    recording = true;
                                                    needReconnect = false;

                                                    if (audio_mix) {
                                                        mStreamer.startMixMusic(mBgmPath, mListener, true);
                                                        mStreamer.setHeadsetPlugged(true);
                                                    }
                                                }
                                            }
                                        }
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }

                            });
                        }
                    }
                    if (mHandler != null) {
                        mHandler.obtainMessage(what, msg).sendToTarget();
                    }
            }
        }
    };
    private OnProgressListener mListener = new OnProgressListener() {
        @Override
        public void onMusicProgress(long currTimeMsec) {
            LogUtils.d("The progress of the currently playing music:" + currTimeMsec);
        }

        @Override
        public void onMusicStopped() {
            LogUtils.d("End of the currently playing music");
        }
    };
    private Chronometer mChronometer;//计时器控件
    private TextView mShootingText;//开始直播
    private TextView mDebugInfoTextView;//显示崩溃信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_record);

        mCameraPreview = (GLSurfaceView) findViewById(R.id.camera_preview);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg != null && msg.obj != null) {
                    String content = msg.obj.toString();
                    switch (msg.what) {
                        case RecorderConstants.KSYVIDEO_CONNECT_FAILED:
                        case RecorderConstants.KSYVIDEO_ENCODED_FRAMES_FAILED:
                        case RecorderConstants.KSYVIDEO_CONNECT_BREAK:
                            Toast.makeText(MyLiveRecordActivity.this, content,
                                    Toast.LENGTH_LONG).show();
                            mChronometer.stop();
                            mShootingText.setText(START_STRING);
                            mShootingText.postInvalidate();
                            break;
                        case RecorderConstants.KSYVIDEO_OPEN_STREAM_SUCC:
                            mChronometer.setBase(SystemClock.elapsedRealtime());
                            // 开始计时
                            mChronometer.start();
                            mShootingText.setText(STOP_STRING);
                            mShootingText.postInvalidate();
                            beginInfoUploadTimer();
                            break;
                        case RecorderConstants.KSYVIDEO_ENCODED_FRAMES_THRESHOLD:
                            mChronometer.stop();
                            recording = false;
                            mShootingText.setText(START_STRING);
                            mShootingText.postInvalidate();
                            Toast.makeText(MyLiveRecordActivity.this, content,
                                    Toast.LENGTH_LONG).show();
                            break;
                        case RecorderConstants.KSYVIDEO_INIT_DONE:
                            if (mShootingText != null)
                                mShootingText.setEnabled(true);
                            Toast.makeText(getApplicationContext(), "初始化完成", Toast.LENGTH_SHORT).show();
                            LogUtils.e("---------KSYVIDEO_INIT_DONE");
                            //							if(!checkoutPreviewStarted()){
                            //								return;
                            //							}
                            if (startAuto && mStreamer.startStream()) {
                                mShootingText.setText(STOP_STRING);
                                mShootingText.postInvalidate();
                                recording = true;
                                if (audio_mix) {
                                    mStreamer.startMixMusic(mBgmPath, mListener, true);
                                    mStreamer.setHeadsetPlugged(true);
                                }
                            } else {
                                LogUtils.e("操作太频繁");
                            }
                            break;
                        default:
                            Toast.makeText(MyLiveRecordActivity.this, content,
                                    Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
        mUrl = "rtmp://test.uplive.ks-cdn.com/live/androidtest?signature=";

        mChronometer = (Chronometer) findViewById(R.id.chronometer);
        mShootingText = (TextView) findViewById(R.id.click_to_shoot);
        mShootingText.setClickable(true);
        mShootingText.setOnClickListener(this);

        builder = new KSYStreamerConfig.Builder();
        builder.setSampleAudioRateInHz(44100);//设置音频采样率 采样频率一般共分为22.05KHz、44.1KHz、48KHz三个等级，22.05KHz只能达到FM广播的声音品质，44.1KHz则是理论上的CD音质界限，48KHz则更加精确一些。
        builder.setFrameRate(15);//设置推流编码帧率

        builder.setMinAverageVideoBitrate(800 * 2 / 8);//设置视频编码码率
        builder.setInitAverageVideoBitrate(800);//设置视频编码码率
        builder.setMaxAverageVideoBitrate(800 * 5 / 8);//设置视频编码码率

        builder.setAudioBitrate(32);//设置音频编码码率
        builder.setVideoResolution(RecorderConstants.VIDEO_RESOLUTION_480P);//设置分辨率等级
        builder.setmUrl(mUrl);//设置推流地址
        builder.setAppId(KSYKey.APPID);//设置AppId,用于SDK鉴权
        builder.setAccessKey(KSYKey.ACCESSKEY);//设置AccessKey，用户SDK鉴权
        builder.setSecretKeySign(KSYKey.SECRETKEY);//设置SecretKeySign，用于SDK鉴权
        builder.setTimeSecond(String.valueOf(System.currentTimeMillis() / 1000));//设置时间戳，用于SDK鉴权
        builder.setAutoAdjustBitrate(true);//是否打开自适应码率功能，默认打开
        //        builder.setStartPreviewManual();//设置手动启动预览，除非调用startCameraPreview接口，否则不自动预览，默认关闭
        builder.setFrontCameraMirror(true);//设置开启前置摄像头镜像，默认关闭
        //        builder.setBeautyFilter();//设置内置美颜类别，目前软编只支持一种
        builder.setManualFocus(true);//设置开启手指指定对焦测光区域，默认关闭
        builder.setEncodeMethod(KSYStreamerConfig.ENCODE_METHOD.HARDWARE);//设置软编还是硬编
        builder.setEnableStreamStatModule(true);
        //设置屏幕横竖屏，默认为竖屏
        if (false) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        builder.setMuteAudio(true);//静音开关

        mStreamer = new KSYStreamer(this);
        mStreamer.setConfig(builder.build());
        mStreamer.setDisplayPreview(mCameraPreview);
        mStreamer.setOnStatusListener(mOnErrorListener);//添加监听器


    }

    @Override
    protected void onResume() {
        super.onResume();
        mStreamer.onResume();
        mAcitivityResumed = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mStreamer.onPause();
        mAcitivityResumed = false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.click_to_shoot:
                if (recording) {
                    if (mStreamer.stopStream()) {
                        if (audio_mix) {//是否开启混音
                            mStreamer.stopMixMusic();
                        }
                        mChronometer.stop();
                        mShootingText.setText(START_STRING);
                        mShootingText.postInvalidate();
                        recording = false;
                    } else {
                        LogUtils.e("操作太频繁");
                    }
                } else {
                    if (mStreamer.startStream()) {
                        mShootingText.setText(STOP_STRING);
                        mShootingText.postInvalidate();
                        recording = true;

                        mStreamer.setEnableReverb(true);
                        mStreamer.setReverbLevel(4);

                        if (audio_mix) {
                            mStreamer.startMixMusic(mBgmPath, mListener, true);
                            mStreamer.setHeadsetPlugged(true);
                        }
                    } else {
                        LogUtils.e("操作太频繁");
                    }
                }
                break;
        }
    }

    /**
     * 开始记录信息
     */
    private void beginInfoUploadTimer() {
        if (printDebugInfo && timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    updateDebugInfo();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mDebugInfoTextView.setText(mDebugInfo);
                        }
                    });
                }
            }, 100, 3000);
        }
    }

    /**
     * 更新记录信息
     */
    private void updateDebugInfo() {
        if (mStreamer == null)
            return;
        mDebugInfo = String.format("RtmpHostIP()=%s DroppedFrameCount()=%d \n " +
                        "ConnectTime()=%d DnsParseTime()=%d \n " +
                        "UploadedKB()=%d EncodedFrames()=%d \n" +
                        "CurrentBitrate=%f Version()=%s",
                mStreamer.getRtmpHostIP(), mStreamer.getDroppedFrameCount(),
                mStreamer.getConnectTime(), mStreamer.getDnsParseTime(),
                mStreamer.getUploadedKBytes(), mStreamer.getEncodedFrames(),
                mStreamer.getCurrentBitrate(), mStreamer.getVersion());
    }
}
