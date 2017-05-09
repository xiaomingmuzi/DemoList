package com.lixm.chat.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lixm.chat.listener.MyReceiveMsgListener;

import org.xutils.x;

import io.rong.imkit.RongIM;


/**
 * User: LXM
 * Date: 2016-03-10
 * Time: 10:01
 * Detail:
 */
public class BaseFragment extends Fragment {
    private boolean injected = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        injected = true;
        return x.view().inject(this, inflater, container);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!injected) {
            x.view().inject(this, this.getView());
        }
        /**
         *  设置接收消息的监听器。
         */
        RongIM.setOnReceiveMessageListener(new MyReceiveMsgListener(getActivity()));
    }
}
