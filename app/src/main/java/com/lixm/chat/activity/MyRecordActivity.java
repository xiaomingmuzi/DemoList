package com.lixm.chat.activity;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

import com.ksyun.ks3.exception.Ks3Error;
import com.ksyun.ks3.model.acl.CannedAccessControlList;
import com.ksyun.ks3.services.Ks3Client;
import com.ksyun.ks3.services.Ks3ClientConfiguration;
import com.ksyun.ks3.services.handler.CreateBucketResponceHandler;
import com.ksyun.ks3.services.handler.PutBucketACLResponseHandler;
import com.ksyun.ks3.services.handler.PutObjectACLResponseHandler;
import com.ksyun.ks3.services.handler.PutObjectResponseHandler;
import com.ksyun.ks3.services.request.PutBucketACLRequest;
import com.ksyun.ks3.services.request.PutObjectRequest;
import com.lixm.chat.R;
import com.lixm.chat.util.KSYKey;

import org.apache.http.Header;
import org.xutils.common.util.LogUtils;

import java.io.File;
import java.io.IOException;

/**
 * class name：TestBasicVideo<BR>
 * class description：一个简单的录制视频例子<BR>
 * PS：实现基本的录制保存文件 <BR>
 *
 * @author LXM
 * @version 1.00 2016/05/03
 */
public class MyRecordActivity extends Activity implements SurfaceHolder.Callback, OnClickListener {
    private Button start;// 开始录制按钮
    private Button stop;// 停止录制按钮
    private Button createBuket;//创建Buket
    private Button delBuket;//删除Buket
    private Button addAcl;//添加ACL权限
    private Button getObject;//获取对象
    private MediaRecorder mediarecorder;// 录制视频的类
    private SurfaceView surfaceview;// 显示视频的控件
    // 用来显示视频的一个接口，我靠不用还不行，也就是说用mediarecorder录制视频还得给个界面看
    // 想偷偷录视频的同学可以考虑别的办法。。嗯需要实现这个接口的Callback接口
    private SurfaceHolder surfaceHolder;
    private Chronometer chronometer;
    private Ks3Client client;
    private Ks3ClientConfiguration configuration;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        // 设置横屏显示
        //                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        // 选择支持半透明模式,在有surfaceview的activity中使用。
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(R.layout.activity_record);
        init();
    }

    private void init() {
        start = (Button) this.findViewById(R.id.start);
        stop = (Button) this.findViewById(R.id.stop);
        createBuket = (Button) findViewById(R.id.createBut);
        delBuket = (Button) findViewById(R.id.delBut);
        addAcl = (Button) findViewById(R.id.addAcl);
        getObject = (Button) findViewById(R.id.getObject);
        start.setOnClickListener(this);
        stop.setOnClickListener(this);
        createBuket.setOnClickListener(this);
        delBuket.setOnClickListener(this);
        addAcl.setOnClickListener(this);
        getObject.setOnClickListener(this);
        surfaceview = (SurfaceView) this.findViewById(R.id.surfaceview);
        SurfaceHolder holder = surfaceview.getHolder();// 取得holder
        holder.addCallback(this); // holder加入回调接口
        // setType必须设置，要不出错.
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        chronometer = (Chronometer) findViewById(R.id.chronometer);

        /* Directly using ak&sk */
        client = new Ks3Client(KSYKey.STOREAK, KSYKey.STORESK, MyRecordActivity.this);
        configuration = Ks3ClientConfiguration.getDefaultConfiguration();
        client.setConfiguration(configuration);
    }

    @Override
    public void onClick(View v) {
        if (v == start) {
            startRecorder();
        } else if (v == stop) {
            stopRecorder();
        } else if (v == createBuket) {//创建Buket
            //创建一个Bucket
            createBucket();
        } else if (v == addAcl) {
            //添加Bucket的ACL权限
            addAcl();
        } else if (v == getObject) {
            //            client.getObject(request, new GetObjectResponseHandler(new File("/sdcard/test1.mp4"),
            //                    "androidtest", "test") {
            //
            //                @Override
            //                public void onTaskSuccess(int paramInt,
            //                                          Header[] paramArrayOfHeader,
            //                                          GetObjectResult getObjectResult) {
            //
            //                }
            //
            //                @Override
            //                public void onTaskStart() {
            //                  //开始下载
            //
            //                }
            //
            //                @Override
            //                public void onTaskProgress(double progress) {
            //                    //下载进度
            //                }
            //
            //                @Override
            //                public void onTaskFinish() {
            //                   //下载完成
            //                }
            //
            //                @Override
            //                public void onTaskCancel() {
            //                    Log.d(com.ksyun.ks3.util.Constants.LOG_TAG, "cancle ok");
            //                }
            //
            //                @Override
            //                public void onTaskFailure(int paramInt, Ks3Error ks3Error,
            //                                          Header[] paramArrayOfHeader, Throwable paramThrowable,
            //                                          File paramFile) {
            //                  //下载失败
            //                }
            //            });
        }
    }

    private void addAcl() {
        PutBucketACLRequest request = new PutBucketACLRequest("androidtest");
        // AccessControlList acl = new AccessControlList();
        // // GranteeUri urigrantee = GranteeUri.AllUsers;
        // // Permission permission = Permission.Read;
        //
        // GranteeEmail email = new GranteeEmail();
        // email.setEmail("guoli@gmail.com");
        // Permission permission = Permission.Read;
        // Grant g = new Grant(email, permission);
        //
        // GranteeUri uirGroup = GranteeUri.AllUsers;
        // Permission uripermission = Permission.Read;
        // Grant g1 = new Grant(uirGroup, uripermission);

        // acl.addGrant(g);
        // acl.addGrant(g1);

        // GranteeId grantee = new GranteeId() ;
        // grantee.setIdentifier("12773456");
        // grantee.setDisplayName("guoliTest222");
        // acl.addGrant(grantee, Permission.Read);

        // GranteeId grantee1 = new GranteeId() ;
        // grantee1.setIdentifier("123005789");
        // grantee1.setDisplayName("guoliTest2D2");
        // acl.addGrant(grantee1, Permission.Write);

        // request.setAccessControlList(acl) ;

        CannedAccessControlList cannedAcl = CannedAccessControlList.PublicReadWrite;
        request.setCannedAcl(cannedAcl);
        // request.setAccessControlList(acl);
        client.putBucketACL(request, new PutBucketACLResponseHandler() {

            @Override
            public void onSuccess(int statesCode,
                                  Header[] responceHeaders) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer
                        .append("Put Bucket ACL success, states code :"
                                + statesCode);
                LogUtils.i("添加ACL权限成功----》" + stringBuffer.toString());
                //                    Intent intent = new Intent(MainActivity.this,
                //                            RESTAPITestResult.class);
                //                    Bundle data = new Bundle();
                //                    data.putString(RESULT, stringBuffer.toString());
                //                    data.putString(API, "Put Bucket ACL Result");
                //                    intent.putExtras(data);
                //                    startActivity(intent);
            }

            @Override
            public void onFailure(int statesCode, Ks3Error error,
                                  Header[] responceHeaders, String response,
                                  Throwable paramThrowable) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(
                        "PUT Bucket ACL FAIL, states code :"
                                + statesCode).append("\n").append("responce :").append(response);
                stringBuffer.append("Exception :"
                        + paramThrowable.toString());
                LogUtils.i("添加ACL权限失败----》" + stringBuffer.toString());
            }
        });
    }

    private void createBucket() {
        client.createBucket("androidtest", new CreateBucketResponceHandler() {
            @Override
            public void onFailure(int statesCode, Ks3Error error, Header[] responceHeaders, String response, Throwable paramThrowable) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(
                        "Delete fail , states code :" + statesCode)
                        .append("\n").append("responce :").append(response);
                stringBuffer.append("Exception :"
                        + paramThrowable.toString());
                Toast.makeText(MyRecordActivity.this, "创建Buket失败！！！", Toast.LENGTH_SHORT).show();
                LogUtils.i("创建失败----》" + stringBuffer.toString());
            }

            @Override
            public void onSuccess(int statesCode, Header[] responceHeaders) {
                Toast.makeText(MyRecordActivity.this, "创建Buket成功！！！", Toast.LENGTH_SHORT).show();
                LogUtils.i("创建成功====" + statesCode);
            }
        });
    }

    private void stopRecorder() {
        if (mediarecorder != null) {
            // 停止录制
            mediarecorder.stop();
            chronometer.stop();
            // 释放资源
            mediarecorder.release();
            mediarecorder = null;

            //上传到bucket中
            //            String bucketname, String objectkey, File file, PutObjectResponseHandler handler
            File file = new File("/sdcard/test1.mp4");
            if (!file.exists())
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            addObject(file);
        }
    }

    private void addObject(File file) {
        PutObjectRequest request = new PutObjectRequest("androidtest", "test1.mp4", file);
//        HashMap<String,String> map=new HashMap<>();
//        request.setHeader();
        client.putObject(request, new PutObjectResponseHandler() {
            @Override
            public void onTaskFailure(int i, Ks3Error ks3Error, Header[] headers, String s, Throwable throwable) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(
                        "upload file failure,file = "
                                + "/sdcard/test1.mp4"
                                + ",states code = "
                                + i).append(
                        "\n").append("response:").append(s);
                LogUtils.i("添加文件失败=========》" + stringBuffer.toString());
            }

            @Override
            public void onTaskSuccess(int i, Header[] headers) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(
                        "upload file success,file = "
                                + "/sdcard/test1.mp4"
                                + ",states code = "
                                + i).append(
                        "\n");
                LogUtils.i("添加对象成功==========》" +
                        stringBuffer.toString() + headers[0].toString());
                //添加对象访问权限
                CannedAccessControlList list = CannedAccessControlList.PublicRead;
                client.putObjectACL("androidtest", "test1.mp4", list, new PutObjectACLResponseHandler() {
                            @Override
                            public void onFailure(int i, Ks3Error ks3Error, Header[] headers, String s, Throwable throwable) {

                            }

                            @Override
                            public void onSuccess(int statesCode, Header[] responceHeaders) {
                                LogUtils.i("添加对象权限成功========"+statesCode);
                                for (int i=0;i<responceHeaders.length;i++){
                                    LogUtils.i("--->"+i+"===="+responceHeaders[i]);
                                }
                            }
                        }
                );
            }

            @Override
            public void onTaskStart() {

            }

            @Override
            public void onTaskFinish() {

            }

            @Override
            public void onTaskCancel() {

            }

            @Override
            public void onTaskProgress(double v) {

            }
        });
    }

    private void startRecorder() {
        mediarecorder = new MediaRecorder();// 创建mediarecorder对象
        // 设置录制视频源为Camera(相机)
        Camera camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        if (camera != null) {
            camera.setDisplayOrientation(90);//摄像图旋转90度
            try {
                camera.setPreviewDisplay(surfaceHolder);
            } catch (IOException e) {
                e.printStackTrace();
            }
            camera.startPreview();
            camera.unlock();
            mediarecorder.setCamera(camera);
        }
        //设置视频旋转90度
        mediarecorder.setOrientationHint(90);
        // 设置录制视频源为Camera(相机)
        mediarecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        //设置麦克风源进行录音
        mediarecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        // 设置录制完成后视频的封装格式THREE_GPP为3gp.MPEG_4为mp4
        mediarecorder
                .setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        // 设置录制的视频编码h263 h264
        mediarecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        // 设置视频录制的分辨率。必须放在设置编码和格式的后面，否则报错  175*144
        mediarecorder.setVideoSize(1280, 720);
        // 设置录制的视频帧率。必须放在设置编码和格式的后面，否则报错
        mediarecorder.setVideoFrameRate(15);
        //设置音频的格式
        mediarecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        //设置预览
        mediarecorder.setPreviewDisplay(surfaceHolder.getSurface());
        // 设置视频文件输出的路径
        mediarecorder.setOutputFile("/sdcard/test1.mp4");
        try {
            // 准备录制
            mediarecorder.prepare();
            // 开始录制
            mediarecorder.start();
            chronometer.setBase(SystemClock.elapsedRealtime());
            // 开始计时
            chronometer.start();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // 将holder，这个holder为开始在oncreat里面取得的holder，将它赋给surfaceHolder
        surfaceHolder = holder;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // 将holder，这个holder为开始在oncreat里面取得的holder，将它赋给surfaceHolder
        surfaceHolder = holder;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // surfaceDestroyed的时候同时对象设置为null
        surfaceview = null;
        surfaceHolder = null;
        mediarecorder = null;
    }
}
