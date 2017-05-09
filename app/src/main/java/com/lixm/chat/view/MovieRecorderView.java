package com.lixm.chat.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
import android.os.Build;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.lixm.chat.R;

import org.xutils.common.util.LogUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * User: LXM
 * Date: 2016-05-06
 * Time: 15:04
 * Detail:视频播放控件
 */
public class MovieRecorderView extends RelativeLayout implements OnErrorListener, View.OnClickListener {

    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private ProgressBar mProgressBar;
    private Button mChange;

    private MediaRecorder mMediaRecorder;
    private Camera mCamera;
    private Timer mTimer;// 计时器
    private OnRecordFinishListener mOnRecordFinishListener;// 录制完成回调接口
//    private RecordActivity.OnStartOrStopListener mOnStartOrStopListener;//开始结束监听回调

    private int mWidth;// 视频分辨率宽度
    private int mHeight;// 视频分辨率高度
    private boolean isOpenCamera;// 是否一开始就打开摄像头
    private int mRecordMaxTime;// 一次拍摄最长时间
    private int mTimeCount;// 时间计数
    private File mVecordFile = null;// 文件

    private boolean isFront = false;

    public MovieRecorderView(Context context) {
        this(context, null);
    }

    public MovieRecorderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MovieRecorderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MovieRecorderView, defStyle, 0);
        mWidth = a.getInteger(R.styleable.MovieRecorderView_width_dp, 640);// 默认320
        mHeight = a.getInteger(R.styleable.MovieRecorderView_height_dp, 480);// 默认240

        isOpenCamera = a.getBoolean(R.styleable.MovieRecorderView_is_open_camera, true);// 默认打开
        mRecordMaxTime = a.getInteger(R.styleable.MovieRecorderView_record_max_time, 10);// 默认为10

        LayoutInflater.from(context).inflate(R.layout.movie_recorder_view, this);
        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceview);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setMax(mRecordMaxTime);// 设置进度条最大量

        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(new CustomCallBack());
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        a.recycle();

        RelativeLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, TRUE);
        addView(LayoutInflater.from(context).inflate(R.layout.recorderview_head, null), params);
        findViewById(R.id.back).setOnClickListener(this);
        mChange = (Button) findViewById(R.id.change);
        mChange.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                freeCameraResource();
                mOnRecordFinishListener.onClickfinish();
                break;
            case R.id.change:
                isFront = !isFront;
                if (isFront) {
                    mChange.setText("后");
                } else {
                    mChange.setText("前");
                }
                if (isOpenCamera) {
                    freeCameraResource();
                }
                try {
                    initCamera();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * 回调接口
     */
    private class CustomCallBack implements Callback {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if (!isOpenCamera)
                return;
            try {
                initCamera();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (!isOpenCamera)
                return;
            freeCameraResource();
        }

    }

    /**
     * 初始化摄像头
     *
     * @throws IOException
     */
    private void initCamera() throws IOException {
        if (mCamera != null) {
            freeCameraResource();
        }
        try {
            if (isFront)
                mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
            else
                mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        } catch (Exception e) {
            e.printStackTrace();
            freeCameraResource();
        }
        if (mCamera == null)
            return;

        setCameraParams();
        mCamera.setDisplayOrientation(90);
        mCamera.setPreviewDisplay(mSurfaceHolder);
        mCamera.startPreview();
        mCamera.unlock();
    }

    /**
     * 设置摄像头竖屏
     */
    private void setCameraParams() {
        if (mCamera != null) {
            Parameters params = mCamera.getParameters();
            params.set("orientation", "portrait");
            mCamera.setParameters(params);
            List<Camera.Size> sizes = params.getSupportedPreviewSizes();
            for (int i = 0; i < sizes.size(); i++) {
                LogUtils.i("===" + i + "===" + sizes.get(i).width + "=====" + sizes.get(i).height);
            }
        }
    }

    /**
     * 释放摄像头资源
     *
     * @author liuyinjun
     * @date 2015-2-5
     */
    private void freeCameraResource() {
        try {
            if (mCamera != null) {
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();
                mCamera.lock();
                mCamera.release();
                mCamera = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建存储文件路径
     */
    private void createRecordDir() {
        File sampleDir = new File(Environment.getExternalStorageDirectory() + File.separator + "com.cnfol.financialplanner/video/");
        if (!sampleDir.exists()) {
            sampleDir.mkdirs();
        }
        File vecordDir = sampleDir;
        // 创建文件
        try {
            mVecordFile = File.createTempFile("and", ".mp4", vecordDir);//mp4格式
        } catch (Exception e) {
            mVecordFile = new File(vecordDir, "and.mp4");
            e.printStackTrace();
        }
    }

    /**
     * 初始化视频录制
     *
     * @throws IOException
     */
    //    private void initRecord() throws IOException {
    //        mMediaRecorder = new MediaRecorder();
    //        mMediaRecorder.reset();
    //        if (mCamera != null)
    //            mMediaRecorder.setCamera(mCamera);
    //        mMediaRecorder.setOnErrorListener(this);
    //        mMediaRecorder.setVideoSource(VideoSource.CAMERA);// 视频源
    //        mMediaRecorder.setAudioSource(AudioSource.MIC);// 音频源
    //        mMediaRecorder.setOutputFormat(OutputFormat.MPEG_4);// 视频输出格式
    //        LogUtils.i("分辨率大小===》" + mWidth + "===" + mHeight);
    //        mMediaRecorder.setVideoSize(mWidth, mHeight);// 设置分辨率：
    //         mMediaRecorder.setVideoFrameRate(16);// 这个我把它去掉了，感觉没什么用
    //        mMediaRecorder.setVideoEncodingBitRate(5 * 1920 * 1080);// 设置帧频率，然后就清晰了
    ////                mMediaRecorder.setVideoEncodingBitRate(1 * 1024 * 512);// 设置帧频率，然后就清晰了
    //        mMediaRecorder.setOrientationHint(90);// 输出旋转90度，保持竖屏录制
    //        mMediaRecorder.setAudioEncoder(AudioEncoder.AMR_NB);// 音频格式
    ////        mMediaRecorder.setVideoEncoder(VideoEncoder.MPEG_4_SP);// 视频录制格式
    //        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
    //        //        mMediaRecorder.setMaxDuration(Constant.MAXVEDIOTIME * 1000);
    //        mMediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
    //        mMediaRecorder.setOutputFile(mVecordFile.getAbsolutePath());
    //        mMediaRecorder.prepare();
    //        try {
    //            mMediaRecorder.start();
    //        } catch (IllegalStateException e) {
    //            e.printStackTrace();
    //        } catch (RuntimeException e) {
    //            e.printStackTrace();
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //        }
    //    }
    private void initRecord() {
        try {
            if (mMediaRecorder == null) {
                mMediaRecorder = new MediaRecorder();// 创建mediarecorder对象
                mMediaRecorder.reset();
                if (mCamera != null)
                    mMediaRecorder.setCamera(mCamera);
                mMediaRecorder.setOnErrorListener(this);
                //设置视频旋转90度
                mMediaRecorder.setOrientationHint(90);
                //设置麦克风源进行录音
                mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                // 设置录制视频源为Camera(相机)
                mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                // 设置录制完成后视频的封装格式THREE_GPP为3gp.MPEG_4为mp4
                mMediaRecorder
                        .setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                //设置音频的格式
                mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                // 设置录制的视频编码h263 h264
                mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
                // 设置视频录制的分辨率。必须放在设置编码和格式的后面，否则报错  175*144
                mMediaRecorder.setVideoSize(1280, 720);
                // 设置录制的视频帧率。必须放在设置编码和格式的后面，否则报错
                mMediaRecorder.setVideoFrameRate(15);
                // 设置视频文件输出的路径
                mMediaRecorder.setOutputFile(mVecordFile.getAbsolutePath());
                mMediaRecorder.setVideoEncodingBitRate(512 * 1000);//设置帧率 512 * 1000    5 * 1920 * 1080
                //设置预览
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mMediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
                }
            } else {
                mMediaRecorder.reset();
            }
            // 准备录制
            mMediaRecorder.prepare();
            // 开始录制
            mMediaRecorder.start();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 达到指定时间之后回调接口
     */
    public void record() {
        createRecordDir();
        try {
            if (!isOpenCamera)// 如果未打开摄像头，则打开
                initCamera();
            initRecord();
            mTimeCount = 0;// 时间计数器重新赋值
            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    mTimeCount++;
                    mProgressBar.setProgress(mTimeCount);// 设置进度条
                }
            }, 0, 1000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止拍摄
     */
    public void stop() {
//        if (mOnRecordFinishListener != null && (mTimeCount >= 5 && mTimeCount <= 60))
//            mOnRecordFinishListener.onRecordFinish();
        stopRecord();
        releaseRecord();
        freeCameraResource();
    }

    /**
     * 停止录制
     */
    public void stopRecord() {
        mProgressBar.setProgress(0);
        if (mTimer != null)
            mTimer.cancel();
        if (mMediaRecorder != null) {
            // 设置后不会崩
            mMediaRecorder.setOnErrorListener(null);
            mMediaRecorder.setPreviewDisplay(null);
            try {
                mMediaRecorder.stop();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (RuntimeException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 释放资源
     */
    private void releaseRecord() {
        if (mMediaRecorder != null) {
            mMediaRecorder.setOnErrorListener(null);
            try {
                mMediaRecorder.release();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mMediaRecorder = null;
    }

    public int getTimeCount() {
        return mTimeCount;
    }

    /**
     * @return the mVecordFile
     */
    public File getmVecordFile() {
        return mVecordFile;
    }

    /**
     * 录制完成回调接口
     *
     * @author liuyinjun
     * @date 2015-2-5
     */
    public interface OnRecordFinishListener {
//        public void onRecordFinish();

        public void onClickfinish();
    }

    @Override
    public void onError(MediaRecorder mr, int what, int extra) {
        try {
            freeCameraResource();
            if (mr != null) {
                mr.reset();
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    /**
//     * 设置监听事件
//     *
//     * @param onRecordFinishListener
//     */
//    public void setmOnRecordFinishListener(OnRecordFinishListener onRecordFinishListener) {
//        this.mOnRecordFinishListener = onRecordFinishListener;
//    }

//    public void setOnStartOrStopListener(RecordActivity.OnStartOrStopListener onStartOrStopListener) {
//        this.mOnStartOrStopListener = onStartOrStopListener;
//    }
}
