package com.lixm.chat.listener;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.lixm.chat.bean.ReceiverBean;
import com.lixm.chat.bean.UserInfo;
import com.lixm.chat.util.PARAM;
import com.lixm.chat.util.SharedPrefrenceUtils;

import org.xutils.common.util.LogUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;
import io.rong.message.ImageMessage;
import io.rong.message.RichContentMessage;
import io.rong.message.TextMessage;

/**
 * User: LXM
 * Date: 2015-10-27
 * Time: 17:03
 * Detail:融云消息接收监听器
 */
public class MyReceiveMsgListener implements RongIMClient.OnReceiveMessageListener {

    private Context context;
    private SharedPrefrenceUtils spu;
    private UserInfo mUserInfo;

    public MyReceiveMsgListener(Context context) {
        this.context = context;
        spu=new SharedPrefrenceUtils(context);
        mUserInfo=spu.getUserInfo();
    }


    /**
     * 收到消息的处理
     *
     * @param message 收到的消息实体
     * @param i       剩余未拉取消息数目
     * @return 收到消息是否处理完成，true表示走自己的处理方式，false走融云默认处理方式
     */
    @Override
    public boolean onReceived(Message message, int i) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String sd = sdf.format(new Date(message.getSentTime()));
        String extra = "";

        Intent intent = new Intent();
        intent.setAction("ReceiveMsg");
        ReceiverBean receiverBean = new ReceiverBean();
        receiverBean.setToUserId(mUserInfo.getUserId());
        receiverBean.setConversationType(message.getConversationType().toString());
        receiverBean.setReceiveTime(sd);
        receiverBean.setSenderUserId(message.getSenderUserId());
        receiverBean.setMessageId(message.getMessageId()+"");
        if (message.getContent() instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message.getContent();
//            收到文本消息类型==》PRIVATE--->>helloExtra-->>hello-->>RC:TxtMsg-->>348392-->>RECEIVE-->>1-->>io.rong.imlib.model.Message$ReceivedStatus@a858452-->>2016-03-10 02:55:36
            LogUtils.i("收到文本消息类型==》" + message.getConversationType() + "--->>" + textMessage.getExtra() + "-->>"
                    + textMessage.getContent() + "-->>" + message.getObjectName() + "-->>"
                    + message.getSenderUserId() + "-->>" + message.getMessageDirection() + "-->>"
                    + message.getMessageId() + "-->>" + message.getReceivedStatus() + "-->>"
                    + sd);
            extra = textMessage.getExtra();
            if (!TextUtils.isEmpty(extra)) {
//                try {
//                    JSONObject jsonObject = new JSONObject(extra);
//                    receiverBean.setUserName(jsonObject.optString("userName"));
//                    receiverBean.setUserHead(jsonObject.optString("userHead"));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                String[] split = extra.split("==");
                receiverBean.setUserName(split[0]);
                receiverBean.setUserHead(split[1]);
            }
            receiverBean.setMsgType(PARAM.TXT);
            receiverBean.setContent(textMessage.getContent());
        } else if (message.getContent() instanceof ImageMessage) {
            ImageMessage imageMessage = (ImageMessage) message.getContent();
            LogUtils.i("收到图片消息--》" + message.getConversationType() + "--->" + imageMessage.getThumUri() + "--->" + imageMessage.getLocalUri() + "--->" + imageMessage.getRemoteUri());
            receiverBean.setThumUri(imageMessage.getRemoteUri() + "");
            receiverBean.setLocalUri(imageMessage.getLocalUri() + "");
            receiverBean.setMsgType(PARAM.IMG);
        } else if (message.getContent() instanceof RichContentMessage) {
            RichContentMessage richContentMessage = (RichContentMessage) message.getContent();
            LogUtils.i("收到图文消息--》" + message.getConversationType() + "--->" + richContentMessage.getContent() + "--->" + richContentMessage.getImgUrl() + "--->" + richContentMessage.getTitle() + "--->" + richContentMessage.getUrl());
            receiverBean.setContent(richContentMessage.getContent());
            receiverBean.setRichTitle(richContentMessage.getTitle());
            receiverBean.setRichUrl(richContentMessage.getUrl());
            receiverBean.setThumUri(richContentMessage.getImgUrl());
            receiverBean.setMsgType(PARAM.TXTANDIMG);
        }
        intent.putExtra("ReceiverBean", receiverBean);
        context.sendBroadcast(intent);

        return false;
    }

}
