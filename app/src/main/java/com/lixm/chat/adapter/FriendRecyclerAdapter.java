package com.lixm.chat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Date: 2016-04-18
 * Time: 14:50
 * Detail:好友RecyclerView适配器
 */
public class FriendRecyclerAdapter extends RecyclerView.Adapter<FriendRecyclerAdapter.Holder> {
    private Context mContext;
    private ArrayList<ReceiverBean> mList;
    private UserInfo mUserInfo;

    public FriendRecyclerAdapter(Context mContext, ArrayList<ReceiverBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
        mUserInfo = (new SharedPrefrenceUtils(mContext)).getUserInfo();
    }

    public void bindData(ArrayList<ReceiverBean> mList) {
        this.mList = mList;
    }

    @Override
    public int getItemViewType(int position) {
        if (mUserInfo != null)
            return mList.get(position).getSenderUserId().equals(mUserInfo.getUserId()) ? 1 : 0;
         else
            return 1;
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        Holder holder = null;
        switch (viewType) {
            case 0:
                holder = new Holder(LayoutInflater.from(mContext).inflate(R.layout.friend_left_item, null), viewType);
                break;
            case 1:
                holder = new Holder(LayoutInflater.from(mContext).inflate(R.layout.friend_right_item, null), viewType);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        ReceiverBean receiverBean = mList.get(position);
//        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (getItemViewType(position) == 0) {
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
                holder.lImg.setVisibility(View.VISIBLE);
            }
        } else {
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
                    holder.rContent.setText(receiverBean.getContent());
                    x.image().bind(holder.rImg, receiverBean.getThumUri());
                    holder.rImg.setVisibility(View.VISIBLE);
//                    holder.rImg.setLayoutParams(params);
                    break;
            }
        }
    }

    class Holder extends RecyclerView.ViewHolder {

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
            super(itemView);
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

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
