package com.lixm.chat.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * User: LXM
 * Date: 2015-11-02
 * Time: 11:41
 * Detail:
 */
@Table(name = "ReceiverData")
public class ReceiverBean implements Parcelable {

    @Column(name = "conversationType")
    private String conversationType;
    @Column(name = "content")
    private String content;
    @Column(name = "userName")
    private String userName;
    @Column(name = "userHead")
    private String userHead;
    @Column(name = "senderUserId",isId = true)
    private String senderUserId;//发送者的id
    @Column(name = "toUserId")
    private String toUserId;//接收者的id
    @Column(name = "messageId")
    private String messageId;
    @Column(name = "msgType")
    private int msgType;//1 TXT;2 IMG; 3 TXTANDIMG;
    @Column(name = "thumUri")
    private String thumUri;
    @Column(name = "localUri")
    private String localUri;
    @Column(name = "richTitle")
    private String richTitle;
    @Column(name = "richUrl")
    private String richUrl;
    @Column(name = "receiveTime")
    private String receiveTime;


    public static final Creator<ReceiverBean> CREATOR = new Creator<ReceiverBean>() {
        @Override
        public ReceiverBean createFromParcel(Parcel in) {
            ReceiverBean receiverBean = new ReceiverBean();
            receiverBean.setConversationType(in.readString());
            receiverBean.setContent(in.readString());
            receiverBean.setUserName(in.readString());
            receiverBean.setUserHead(in.readString());
            receiverBean.setSenderUserId(in.readString());
            receiverBean.setToUserId(in.readString());
            receiverBean.setMessageId(in.readString());
            receiverBean.setMsgType(in.readInt());
            receiverBean.setThumUri(in.readString());
            receiverBean.setLocalUri(in.readString());
            receiverBean.setRichTitle(in.readString());
            receiverBean.setRichUrl(in.readString());
            receiverBean.setReceiveTime(in.readString());
            return receiverBean;
        }

        @Override
        public ReceiverBean[] newArray(int size) {
            return new ReceiverBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(conversationType);
        dest.writeString(content);
        dest.writeString(userName);
        dest.writeString(userHead);
        dest.writeString(senderUserId);
        dest.writeString(toUserId);
        dest.writeString(messageId);
        dest.writeInt(msgType);
        dest.writeString(thumUri);
        dest.writeString(localUri);
        dest.writeString(richTitle);
        dest.writeString(richUrl);
        dest.writeString(receiveTime);
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(String receiveTime) {
        this.receiveTime = receiveTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserHead() {
        return userHead;
    }

    public void setUserHead(String userHead) {
        this.userHead = userHead;
    }

    public String getConversationType() {
        return conversationType;
    }

    public String getContent() {
        return content;
    }


    public String getSenderUserId() {
        return senderUserId;
    }

    public String getMessageId() {
        return messageId;
    }

    /**
     * //1 TXT;2 IMG; 3 TXTANDIMG;
     *
     * @return
     */
    public int getMsgType() {
        return msgType;
    }

    public String getThumUri() {
        return thumUri;
    }

    public String getLocalUri() {
        return localUri;
    }

    public String getRichTitle() {
        return richTitle;
    }

    public String getRichUrl() {
        return richUrl;
    }

    public void setConversationType(String conversationType) {
        this.conversationType = conversationType;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public void setSenderUserId(String senderUserId) {
        this.senderUserId = senderUserId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    /**
     * 1 TXT;2 IMG; 3 TXTANDIMG;
     *
     * @param msgType
     */
    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public void setThumUri(String thumUri) {
        this.thumUri = thumUri;
    }

    public void setLocalUri(String localUri) {
        this.localUri = localUri;
    }

    public void setRichTitle(String richTitle) {
        this.richTitle = richTitle;
    }

    public void setRichUrl(String richUrl) {
        this.richUrl = richUrl;
    }

    @Override
    public String toString() {
        return "ReceiverBean{" +
                "conversationType='" + conversationType + '\'' +
                ", content='" + content + '\'' +
                ", userName='" + userName + '\'' +
                ", userHead='" + userHead + '\'' +
                ", senderUserId='" + senderUserId + '\'' +
                ", messageId='" + messageId + '\'' +
                ", msgType=" + msgType +
                ", thumUri='" + thumUri + '\'' +
                ", localUri='" + localUri + '\'' +
                ", richTitle='" + richTitle + '\'' +
                ", richUrl='" + richUrl + '\'' +
                '}';
    }
}
