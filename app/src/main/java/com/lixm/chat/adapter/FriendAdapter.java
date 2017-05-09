package com.lixm.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lixm.chat.R;
import com.lixm.chat.bean.ReceiverBean;
import com.lixm.chat.bean.UserInfo;
import com.lixm.chat.util.SharedPrefrenceUtils;

import org.xutils.x;

import java.util.ArrayList;

/**
 * User: LXM
 * Date: 2016-04-19
 * Time: 15:43
 * Detail:
 */
public class FriendAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<ReceiverBean> mList;
    private UserInfo mUserInfo;

    public FriendAdapter(Context mContext, ArrayList<ReceiverBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
        mUserInfo = (new SharedPrefrenceUtils(mContext)).getUserInfo();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (mUserInfo != null)
            return mList.get(position).getSenderUserId().equals(mUserInfo.getUserId()) ? 1 : 0;
        else
            return 1;
    }

    Holder holder;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ReceiverBean receiverBean = mList.get(position);
        if (getItemViewType(position) == 0) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.friend_left_item, null);
                holder = new Holder(convertView, getItemViewType(position));
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            x.image().bind(holder.lUserHead, receiverBean.getUserHead());
            holder.lUserName.setText(receiverBean.getUserName());
            if (receiverBean.getMsgType() == 1) {//文字
                holder.lContent.setVisibility(View.VISIBLE);
                holder.lContent.setText(receiverBean.getContent());
                holder.lImg.setVisibility(View.GONE);
            } else if (receiverBean.getMsgType() == 2) {//图片
                holder.lContent.setVisibility(View.GONE);
                x.image().bind(holder.lImg, receiverBean.getThumUri());
                holder.lImg.setVisibility(View.VISIBLE);
                //                holder.lImg.setLayoutParams(params);
            } else if (receiverBean.getMsgType() == 3) {//图文->视频
                holder.lContent.setVisibility(View.VISIBLE);
                holder.lContent.setText(receiverBean.getContent());
                x.image().bind(holder.lImg, receiverBean.getThumUri());
                //                holder.lImg.setLayoutParams(params);
            }
        } else {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.friend_right_item, null);
                holder = new Holder(convertView, getItemViewType(position));
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            x.image().bind(holder.rUserHead, receiverBean.getUserHead());
            holder.rUserName.setText(receiverBean.getUserName());
            switch (receiverBean.getMsgType()) {
                case 1://文字
                    holder.rContent.setVisibility(View.VISIBLE);
                    holder.rContent.setText(receiverBean.getContent());
                    holder.rImg.setVisibility(View.GONE);
                    break;
                case 2://图片
                    holder.rContent.setVisibility(View.GONE);
                    x.image().bind(holder.rImg, receiverBean.getThumUri());
                    holder.rImg.setVisibility(View.VISIBLE);
                    //                    holder.rImg.setLayoutParams(params);
                    break;
                case 3://图文->视频
                    holder.rContent.setVisibility(View.VISIBLE);
                    holder.rContent.setText("视频");
                    x.image().bind(holder.rImg, receiverBean.getThumUri());
                    holder.rImg.setVisibility(View.VISIBLE);
                    //                    holder.rImg.setLayoutParams(params);
                    break;
            }
        }
        return convertView;
    }

    class Holder {
        LinearLayout lRoot;
        ImageView lUserHead;
        TextView lUserName;
        TextView lContent;
        ImageView lImg;

        LinearLayout rRoot;
        ImageView rUserHead;
        TextView rUserName;
        TextView rContent;
        ImageView rImg;

        public Holder(View itemView, int viewType) {
            switch (viewType) {
                case 0:
                    lRoot = (LinearLayout) itemView.findViewById(R.id.friend_left_root);
                    lUserHead = (ImageView) itemView.findViewById(R.id.friend_left_head);
                    lUserName = (TextView) itemView.findViewById(R.id.friend_left_name);
                    lContent = (TextView) itemView.findViewById(R.id.friend_left_txt);
                    lImg = (ImageView) itemView.findViewById(R.id.friend_left_img);
                    break;
                case 1:
                    rRoot = (LinearLayout) itemView.findViewById(R.id.friend_right_root);
                    rUserHead = (ImageView) itemView.findViewById(R.id.friend_right_head);
                    rUserName = (TextView) itemView.findViewById(R.id.friend_right_name);
                    rContent = (TextView) itemView.findViewById(R.id.friend_right_txt);
                    rImg = (ImageView) itemView.findViewById(R.id.friend_right_img);
                    break;
            }
        }
    }
}
