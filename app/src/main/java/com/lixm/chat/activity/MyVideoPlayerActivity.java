package com.lixm.chat.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.ksyun.media.player.IMediaPlayer;
import com.ksyun.media.player.KSYMediaPlayer;
import com.lixm.chat.R;
import com.lixm.chat.util.KSYKey;
import com.lixm.chat.view.VideoSurfaceView;

import org.xutils.common.util.MD5;

import java.io.IOException;

/**
 * User: LXM
 * Date: 2016-04-27
 * Time: 15:26
 * Detail:播放视频类
 */
public class MyVideoPlayerActivity extends Activity {

    private KSYMediaPlayer ksyMediaPlayer;
    private IMediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener = new IMediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {

        }
    };
    private IMediaPlayer.OnCompletionListener mOnCompletionListener = new IMediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(IMediaPlayer iMediaPlayer) {

        }
    };
    private IMediaPlayer.OnPreparedListener mOnPreraradListener = new IMediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(IMediaPlayer iMediaPlayer) {
            ksyMediaPlayer.start();
        }
    };
    private IMediaPlayer.OnInfoListener mOnInfoListener = new IMediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
            return false;
        }
    };
    private IMediaPlayer.OnVideoSizeChangedListener mOnVideoSizeChangedListener = new IMediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i1, int i2, int i3) {

        }
    };
    private IMediaPlayer.OnErrorListener mOnErrorListener = new IMediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
            return false;
        }
    };
    private IMediaPlayer.OnSeekCompleteListener mOnSeekCompleteListener = new IMediaPlayer.OnSeekCompleteListener() {
        @Override
        public void onSeekComplete(IMediaPlayer iMediaPlayer) {

        }
    };

    //3.设置视频显示
    private Surface mSurface = null;
    private SurfaceView mVideoSurfaceView = null;
    private SurfaceHolder mSurfaceHolder = null;
    private final SurfaceHolder.Callback mSurfaceCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {

        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            ksyMediaPlayer.setDisplay(holder);
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        //1.初始化，设置id和key
        String timeSec = String.valueOf(System.currentTimeMillis() / 1000);
        String skSign = MD5.md5(KSYKey.SECRETKEY + timeSec);
        ksyMediaPlayer = new KSYMediaPlayer.Builder(this.getApplicationContext())
                .setAppId(KSYKey.APPID)
                .setAccessKey(KSYKey.ACCESSKEY)
                .setSecretKeySign(skSign)
                .setTimeSec(timeSec)
                .build();

        //2.添加监听器
        ksyMediaPlayer.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
        ksyMediaPlayer.setOnCompletionListener(mOnCompletionListener);
        ksyMediaPlayer.setOnPreparedListener(mOnPreraradListener);
        ksyMediaPlayer.setOnInfoListener(mOnInfoListener);
        ksyMediaPlayer.setOnVideoSizeChangedListener(mOnVideoSizeChangedListener);
        ksyMediaPlayer.setOnErrorListener(mOnErrorListener);
        ksyMediaPlayer.setOnSeekCompleteListener(mOnSeekCompleteListener);

        //3.设置视频显示
        mVideoSurfaceView = (VideoSurfaceView) findViewById(R.id.player_surface);
        mSurfaceHolder = mVideoSurfaceView.getHolder();
        mSurfaceHolder.addCallback(mSurfaceCallback);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //4.添加视频链接，进入prapare状态
                try {
                    //            ksyMediaPlayer.setDataSource("http://video.sina.com.cn/share/video/250556456.swf");
                    ksyMediaPlayer.setDataSource("http://zjtestbuket.ks3-cn-beijing.ksyun.com/a.mp4");
//                    ksyMediaPlayer.setDataSource("http://kssws.ks-cdn.com/zjtestbuket1/%E9%98%85%E8%AF%BB%E6%95%B0%E5%8A%9F%E8%83%BD%E9%94%99%E8%AF%AF.mp4");
                    ksyMediaPlayer.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

    }


}
