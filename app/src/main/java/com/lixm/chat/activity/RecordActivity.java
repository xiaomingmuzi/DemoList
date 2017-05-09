package com.lixm.chat.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ksyun.ks3.exception.Ks3Error;
import com.ksyun.ks3.model.acl.CannedAccessControlList;
import com.ksyun.ks3.services.Ks3Client;
import com.ksyun.ks3.services.Ks3ClientConfiguration;
import com.ksyun.ks3.services.handler.PutObjectACLResponseHandler;
import com.ksyun.ks3.services.handler.PutObjectResponseHandler;
import com.ksyun.ks3.services.request.PutObjectRequest;
import com.lixm.chat.R;
import com.lixm.chat.util.KSYKey;
import com.lixm.chat.view.MovieRecorderView;

import org.apache.http.Header;
import org.xutils.common.util.LogUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LXM
 * @date 2016-05-16
 * @detail 视频录制Activity
 */
public class RecordActivity extends Activity implements View.OnTouchListener, MovieRecorderView.OnRecordFinishListener, View.OnClickListener {

    private MovieRecorderView mRecorderView;
    private Button mShootBtn;
    private boolean isFinish = true;
    private boolean isRecording = false;//标示是否是暂停播放

    private Ks3Client client;
    private Ks3ClientConfiguration configuration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_movie);
        mRecorderView = (MovieRecorderView) findViewById(R.id.movieRecorderView);
        //        mRecorderView.setmOnRecordFinishListener(this);
        mShootBtn = (Button) findViewById(R.id.shoot_button);

          /* Directly using ak&sk */
        client = new Ks3Client(KSYKey.STOREAK, KSYKey.STORESK, RecordActivity.this);
        client.setEndpoint("ks3-cn-beijing.ksyun.com");//使用北京的bucket，默认是杭州的bucket
        configuration = Ks3ClientConfiguration.getDefaultConfiguration();
        client.setConfiguration(configuration);

        //        mShootBtn.setOnTouchListener(this);
        mShootBtn.setOnClickListener(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        isFinish = true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        isFinish = false;
        mRecorderView.stop();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    finishActivity((String) msg.obj);
                    break;
            }
        }
    };

    private void finishActivity(String path) {
        if (isFinish) {
            mRecorderView.stop();
            //           startActivity(this, mRecorderView.getmVecordFile().toString());
            Toast.makeText(RecordActivity.this, "录制视频结束！！", Toast.LENGTH_SHORT).show();
            if (!TextUtils.isEmpty(path))
                addObject(path);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //        if (event.getAction() == MotionEvent.ACTION_DOWN) {
        //            mRecorderView.record();
        //        } else if (event.getAction() == MotionEvent.ACTION_UP) {
        //            if (mRecorderView.getTimeCount() > 5) {
        //                handler.sendEmptyMessage(1);
        //            } else {
        //                if (mRecorderView.getmVecordFile() != null)
        //                    mRecorderView.getmVecordFile().delete();
        //                mRecorderView.stop();
        //                Toast.makeText(RecordActivity.this, "视频录制时间太短", Toast.LENGTH_SHORT).show();
        //            }
        //        }
        return true;
    }

    private void addObject(final String path) {
        String objectKey = path.split("/")[path.split("/").length - 1];
        LogUtils.i("添加对象key---》" + objectKey);
        PutObjectRequest request = new PutObjectRequest("zjtestbuket", objectKey, new File(path));
        Map<String, String> customParams = new HashMap<>();
        String callBackBody = "tag=avscrnshot&ss=5&res=640x360&rotate=0|tag=saveas&bucket=zjtestbuket&object=" + objectKey + "&key=" + com.lixm.chat.util.Base64.encode("video/images/" + "and" + ".jpg&kss-userid=348392");
        customParams.put("kss-callBackBody", callBackBody);
        request.setCallBack("http://127.0.0.1:19091/kss/call_back", callBackBody, customParams);
        client.putObject(request, new PutObjectResponseHandler() {
            @Override
            public void onTaskFailure(int i, Ks3Error ks3Error, Header[] headers, String s, Throwable throwable) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(
                        "upload file failure,file = "
                                + path.split("/")[path.split("/").length - 1]
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
                                + path.split("/")[path.split("/").length - 1]
                                + ",states code = "
                                + i).append(
                        "\n");
                LogUtils.i("添加对象成功==========》" +
                        stringBuffer.toString() + headers[0].toString());
                //添加对象访问权限
                CannedAccessControlList list = CannedAccessControlList.PublicRead;
                client.putObjectACL("zjtestbuket", path.split("/")[path.split("/").length - 1], list, new PutObjectACLResponseHandler() {
                            @Override
                            public void onFailure(int i, Ks3Error ks3Error, Header[] headers, String s, Throwable throwable) {

                            }

                            @Override
                            public void onSuccess(int statesCode, Header[] responceHeaders) {
                                LogUtils.i("添加对象权限成功========" + statesCode);
                                //                                for (int i = 0; i < responceHeaders.length; i++) {
                                //                                    LogUtils.i("--->" + i + "====" + responceHeaders[i]);
                                //                                }
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

    //    @Override
    //    public void onRecordFinish() {
    //        Message message = Message.obtain();
    //        message.what = 1;
    //        message.obj = mRecorderView.getmVecordFile().getAbsolutePath();
    //        handler.sendMessage(message);
    //    }

    @Override
    public void onClickfinish() {
        RecordActivity.this.finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shoot_button:
                if (isRecording) {//之前处于录制状态
                    mShootBtn.setText("开始");
                    if (mRecorderView.getTimeCount() > 5) {
                        Message message = Message.obtain();
                        message.what = 1;
                        message.obj = mRecorderView.getmVecordFile().getAbsolutePath();
                        handler.sendMessage(message);
                        //                        handler.sendEmptyMessage(1);
                    } else {
                        if (mRecorderView.getmVecordFile() != null)
                            mRecorderView.getmVecordFile().delete();
                        mRecorderView.stop();
                        Toast.makeText(RecordActivity.this, "视频录制时间太短", Toast.LENGTH_SHORT).show();
                    }
                } else {//点击开始录制
                    mShootBtn.setText("结束");
                    mRecorderView.record();
                }
                isRecording = !isRecording;
                break;
        }
    }

}
