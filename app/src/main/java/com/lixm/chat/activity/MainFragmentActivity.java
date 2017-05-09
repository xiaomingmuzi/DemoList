package com.lixm.chat.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TabHost;

import com.lixm.chat.R;
import com.lixm.chat.bean.UserInfo;
import com.lixm.chat.fragment.FriendFragment;
import com.lixm.chat.fragment.MsgFragment;
import com.lixm.chat.fragment.UserFragment;
import com.lixm.chat.util.PARAM;
import com.lixm.chat.util.SharedPrefrenceUtils;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class MainFragmentActivity extends FragmentActivity implements RadioGroup.OnCheckedChangeListener,
        MsgFragment.OnMsgFragmentInteractionListener, FriendFragment.OnFriendFragmentInteractionListener,
        UserFragment.OnUserFragmentInteractionListener {

    @ViewInject(R.id.bottom_group)
    private RadioGroup mGroup;
    @ViewInject(R.id.realTabContent)
    private LinearLayout mTabContent;
    @ViewInject(android.R.id.tabhost)
    private FragmentTabHost mTabHost;

    private SharedPrefrenceUtils mSpu;
    private UserInfo mUserInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fragment);
        x.view().inject(this);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realTabContent);
        //设置显示的标题
        TabHost.TabSpec tabSpecA = mTabHost.newTabSpec("0").setIndicator("A");
        TabHost.TabSpec tabSpecB = mTabHost.newTabSpec("1").setIndicator("B");
        TabHost.TabSpec tabSpecC = mTabHost.newTabSpec("2").setIndicator("C");
        mTabHost.addTab(tabSpecA, MsgFragment.class, null);
        mTabHost.addTab(tabSpecB, FriendFragment.class, null);
        mTabHost.addTab(tabSpecC, UserFragment.class, null);
        mTabHost.setVisibility(View.GONE);
        mGroup.setOnCheckedChangeListener(this);
        mTabHost.setCurrentTabByTag("0");
        mSpu = new SharedPrefrenceUtils(this);
        mUserInfo = mSpu.getUserInfo();
        if (mUserInfo != null) {
            PARAM.connect(this, mUserInfo.getToken());
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.bottom_msg://消息
                mTabHost.setCurrentTabByTag("0");
                break;
            case R.id.bottom_friend://好友
                mTabHost.setCurrentTabByTag("1");
                break;
            case R.id.bottom_user://登录用户
                mTabHost.setCurrentTabByTag("2");
                break;
        }
    }

    @Override
    public void onMsgFragmentInteraction(Object object) {

    }

    @Override
    public void onFriendFragmentInteraction(Object object) {

    }

    @Override
    public void onUserFragmentInteraction(Object object) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
