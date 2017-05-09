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

import org.xutils.x;

import java.util.ArrayList;

import io.rong.imkit.RongIM;

/**
 * User: LXM
 * Date: 2016-03-09
 * Time: 13:34
 * Detail:消息RecyclerView适配器
 */
public class MsgRecyclerAdapter extends RecyclerView.Adapter<MsgRecyclerAdapter.Holder> {
    private Context mContext;
    private ArrayList<ReceiverBean> mMsgBeans;

    public MsgRecyclerAdapter(Context context, ArrayList<ReceiverBean> msgBeans) {
        this.mContext = context;
        this.mMsgBeans = msgBeans;
    }

    public void bindData(ArrayList<ReceiverBean> mMsgBeans){
        this.mMsgBeans=mMsgBeans;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        Holder holder = new Holder(LayoutInflater.from(mContext).inflate(R.layout.msg_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        holder.userName.setText(mMsgBeans.get(position).getUserName());
        x.image().bind(holder.userHead, mMsgBeans.get(position).getUserHead());
        holder.userContent.setText(mMsgBeans.get(position).getContent());
        holder.time.setText(mMsgBeans.get(position).getReceiveTime());
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //启动会话界面
                if (RongIM.getInstance() != null)
                    RongIM.getInstance().startPrivateChat(mContext, mMsgBeans.get(position).getSenderUserId(), mMsgBeans.get(position).getUserName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMsgBeans == null ? 0 : mMsgBeans.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        LinearLayout root;
        ImageView userHead;
        TextView userName;
        TextView userContent;
        TextView time;

        public Holder(View view) {
            super(view);
            root= (LinearLayout) view.findViewById(R.id.msg_item_root);
            userHead = (ImageView) view.findViewById(R.id.msg_userHead);
            userName = (TextView) view.findViewById(R.id.msg_userName);
            userContent = (TextView) view.findViewById(R.id.msg_userContent);
            time= (TextView) view.findViewById(R.id.msg_time);
        }
    }
}
