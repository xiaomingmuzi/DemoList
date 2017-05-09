package com.lixm.chat.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lixm.chat.R;
import com.lixm.chat.application.APP;
import com.lixm.chat.bean.ReceiverBean;
import com.lixm.chat.bean.UserInfo;
import com.lixm.chat.listener.MyReceiveMsgListener;
import com.lixm.chat.util.PARAM;
import com.lixm.chat.util.UploadImgUtil;
import com.lixm.chat.util.UrlUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtils;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.ImageMessage;
import io.rong.message.RichContentMessage;
import io.rong.message.TextMessage;


public class SecondActivity extends Activity implements View.OnClickListener {
    private UserInfo userInfo;
    private String token;
    private Context context;

    @ViewInject(R.id.showConnect)
    private TextView text;
    @ViewInject(R.id.textEdit)
    private EditText textEdit;
    @ViewInject(R.id.sendText)
    private Button sendText;
    @ViewInject(R.id.progressBar)
    private ProgressBar pb;
    @ViewInject(R.id.sendPic)
    private Button sendPic;
    @ViewInject(R.id.showThumbnailImg)
    private ImageView showThumbnailImg;
    @ViewInject(R.id.showNormalImg)
    private ImageView showNormalImg;
    @ViewInject(R.id.loadImg)
    private ProgressBar loadImg;
    @ViewInject(R.id.sendTxtAndImg)
    private Button sendTxtAndImg;
    @ViewInject(R.id.showTxtImg)
    private TextView showTxtAndImgTxt;
    @ViewInject(R.id.showTxtImgImg)
    private ImageView showTxtImgImg;
    @ViewInject(R.id.reGetCode)
    private Button reGetCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        x.view().inject(this);
        context = SecondActivity.this;
        userInfo = (UserInfo) getIntent().getSerializableExtra("UserInfo");

        sendText.setClickable(false);
        sendText.setOnClickListener(this);
        sendPic.setOnClickListener(this);
        sendTxtAndImg.setOnClickListener(this);
        reGetCode.setOnClickListener(this);
        loadImg.setVisibility(View.GONE);
        netForData();

        registerReceiver(receiver, new IntentFilter("ReceiveMsg"));
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ReceiverBean receiverBean = intent.getParcelableExtra("ReceiverBean");
            LogUtils.i("广播接收到数据-----》" + receiverBean.toString());
            switch (receiverBean.getMsgType()) {
                case PARAM.TXT:
                    StringBuffer sb = new StringBuffer(text.getText().toString().trim());
                    sb.append("\n" + receiverBean.getContent());
                    sb.append("\n" + receiverBean.getUserName());
                    text.setText(sb.toString());
                    break;
                case PARAM.IMG:
                    LogUtils.i(receiverBean.getLocalUri() + "==============" + receiverBean.getThumUri());
                    x.image().bind(showNormalImg, receiverBean.getThumUri());
                    break;
                case PARAM.TXTANDIMG:
                    showTxtAndImgTxt.setText("标题--》" + receiverBean.getRichTitle() + "\nURL--》" + receiverBean.getThumUri() + "\n内容--》" + receiverBean.getContent());
                    x.image().bind(showTxtImgImg, receiverBean.getThumUri());
                    break;
            }


        }
    };

    /**
     * 请求网络，获取Token值
     */
    private void netForData() {
        pb.setVisibility(View.VISIBLE);
        text.setText("正在加载中...");
        RequestParams params = new RequestParams(UrlUtils.GetToken);
        params.addQueryStringParameter("userId", userInfo.getUserId());
        LogUtils.i("获取Token--》" + UrlUtils.GetToken + "?userId=" + userInfo.getUserId());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    reGetCode.setVisibility(View.GONE);
                    LogUtils.i("获取Token数据返回--》" + result);
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.optString("code");
                    if (code.equals("200")) {
                        token = jsonObject.optString("token");
                        pb.setVisibility(View.GONE);
                        text.setText("融云Token值--》" + token);
                        connect(token);
                    }
                } catch (JSONException e) {
                    reGetCode.setVisibility(View.VISIBLE);
                    pb.setVisibility(View.GONE);
                    String txt = text.getText().toString().toString();
                    text.setText(txt + "\n数据解析异常");
                    e.printStackTrace();
                    LogUtils.i("数据解析异常-------》");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(context, "获取Token失败!", Toast.LENGTH_SHORT).show();
                text.setText("获取Token失败!");
                reGetCode.setVisibility(View.VISIBLE);
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 建立与融云服务器的连接
     *
     * @param token
     */
    private void connect(String token) {

        /**
         *  设置接收消息的监听器。
         */
        RongIM.setOnReceiveMessageListener(new MyReceiveMsgListener(SecondActivity.this));

        if (getApplicationInfo().packageName.equals(APP.getCurProcessName(getApplicationContext()))) {

            /**
             * IMKit SDK调用第二步,建立与服务器的连接
             */
            RongIM.connect(token, new RongIMClient.ConnectCallback() {

                /**
                 * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
                 */
                @Override
                public void onTokenIncorrect() {
                    LogUtils.i("--onTokenIncorrect");
                }

                /**
                 * 连接融云成功
                 * @param userid 当前 token
                 */
                @Override
                public void onSuccess(String userid) {
                    LogUtils.i("--onSuccess--" + userid);
                    String txt = text.getText().toString().trim();
                    text.setText(txt + "\n连接成功，当前登录用户id-》" + userid);
                    sendText.setClickable(true);
                }

                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    LogUtils.i("--onError--" + errorCode);
                }
            });
        }
    }

    /**
     * 发送文本消息
     *
     * @param content 消息内容
     * @param extra   消息附加信息
     */
    private void sendText(String content, String extra) {
        /**
         * 发送消息。
         *
         * @param type        会话类型。
         * @param targetId    目标 Id。根据不同的 conversationType，可能是用户 Id、讨论组 Id、群组 Id 或聊天室 Id。 kx 348359  blog 348392
         * @param content     消息内容。
         * @param pushContent push 时提示内容，为空时提示文本内容。
         * @param callback    发送消息的回调。
         * @return
         */
        TextMessage textMessage = TextMessage.obtain(content);
        textMessage.setExtra(extra);
        textMessage.setContent(content);
        RongIM.getInstance().getRongIMClient().sendMessage(Conversation.ConversationType.PRIVATE, userInfo.getUserId().equals("348392") ? "348392" : "348359", textMessage, "", "", new RongIMClient.SendMessageCallback() {
            @Override
            public void onError(Integer messageId, RongIMClient.ErrorCode e) {
                LogUtils.i(e.getMessage());
            }

            @Override
            public void onSuccess(Integer integer) {
                LogUtils.i("发送成功!==>" + integer);
            }
        });
    }

    /**
     * 发送图片
     */
    private void sendPic() {
        ImageMessage imageMessage = UploadImgUtil.getImg(SecondActivity.this);
        if (imageMessage != null) {
            RongIM.getInstance().getRongIMClient().sendImageMessage(Conversation.ConversationType.PRIVATE, userInfo.getUserId(), imageMessage, "", "", new RongIMClient.SendImageMessageCallback() {
                @Override
                public void onAttached(Message message) {
                    LogUtils.i("---图片----onAttached------" + message.getContent());
                }

                @Override
                public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                    LogUtils.i("---图片----onError------" + message.getContent());
                }

                @Override
                public void onSuccess(Message message) {
                    ImageMessage imageMessage = (ImageMessage) message.getContent();
                    LogUtils.i("---图片----onSuccess------" + imageMessage.getLocalUri() + "--------" + imageMessage.getThumUri());
                }

                @Override
                public void onProgress(Message message, int i) {
                    loadImg.setProgress(i);
                }
            });
        } else {
            Toast.makeText(SecondActivity.this, "获取图片失败!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 图文发送
     */
    private void setSendTxtAndImg() {
        RichContentMessage richContentMessage = RichContentMessage.obtain("我是标题", "我是内容内容是内容", "http://p6.qhimg.com/t01275cd56e245990c5.png");
        RongIM.getInstance().getRongIMClient().sendMessage(Conversation.ConversationType.PRIVATE, userInfo.getUserId(), richContentMessage, "推送内容，", "推送data", new RongIMClient.SendMessageCallback() {
            @Override
            public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {

            }

            @Override
            public void onSuccess(Integer integer) {
                LogUtils.i("图文发送成功--》" + integer);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendText:
                String content = textEdit.getText().toString().trim();
                String extra = userInfo.toString();
                sendText(content, extra);
                break;
            case R.id.sendPic:
                sendPic();
                loadImg.setVisibility(View.VISIBLE);
                break;
            case R.id.sendTxtAndImg:
                setSendTxtAndImg();
                break;
            case R.id.reGetCode:
                netForData();
                break;
        }
    }
}
