package com.lixm.chat.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lixm.chat.R;
import com.lixm.chat.activity.MyLiveRecordActivity;
import com.lixm.chat.activity.MyVideoPlayerActivity;
import com.lixm.chat.activity.RecoreCamera2Activity;
import com.lixm.chat.bean.UserInfo;
import com.lixm.chat.util.PARAM;
import com.lixm.chat.util.SharedPrefrenceUtils;
import com.lixm.chat.util.UrlUtils;
import com.lixm.chat.zxing.activity.CaptureActivity;
import com.lixm.chat.zxing.encoding.EncodingUtils;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtils;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnUserFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@ContentView(R.layout.fragment_user)
public class UserFragment extends BaseFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnUserFragmentInteractionListener mListener;
    private SharedPrefrenceUtils mSpu;
    private UserInfo mUserInfo;

    @ViewInject(R.id.userName)
    private EditText mUserName;
    @ViewInject(R.id.password)
    private EditText mPassWord;
    @ViewInject(R.id.login)
    private Button mLogin;
    @ViewInject(R.id.wx_login)
    private Button mWxLogin;
    @ViewInject(R.id.loginLayout)
    private RelativeLayout mLoginLayout;
    @ViewInject(R.id.loginSucces)
    private RelativeLayout mLoginSucces;
    @ViewInject(R.id.userHead)
    private ImageView mUserHead;
    @ViewInject(R.id.userNickName)
    private TextView mUserNickName;
    @ViewInject(R.id.userPhone)
    private TextView mUserPhone;
    @ViewInject(R.id.userGold)
    private TextView mUserGold;
    @ViewInject(R.id.exit)
    private Button mExit;
    @ViewInject(R.id.decode)
    private Button mDecode;
    @ViewInject(R.id.decode_img)
    private ImageView mDecodeImg;
    @ViewInject(R.id.encode)
    private Button mEncode;
    @ViewInject(R.id.play)
    private Button mPlay;
    @ViewInject(R.id.record)
    private Button mRecord;
    @ViewInject(R.id.testRecord)
    private Button mTestRecord;


    public UserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mSpu = new SharedPrefrenceUtils(getActivity());
        mUserInfo = mSpu.getUserInfo();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLogin.setOnClickListener(this);
        mWxLogin.setOnClickListener(this);
        mExit.setOnClickListener(this);
        mDecode.setOnClickListener(this);
        mEncode.setOnClickListener(this);
        mPlay.setOnClickListener(this);
        mRecord.setOnClickListener(this);
        mTestRecord.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mUserInfo != null) {
            showUserMsg(mUserInfo);
        } else {
            mLoginLayout.setVisibility(View.VISIBLE);
            mLoginSucces.setVisibility(View.GONE);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onUserFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnUserFragmentInteractionListener) {
            mListener = (OnUserFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnUserFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login://登录
                netForLogin(mUserName.getText().toString().trim(), mPassWord.getText().toString().trim());
                break;
            case R.id.wx_login://微信登录
                break;
            case R.id.exit://�?出登�?
                mSpu.deleteUserInfo();
                mLoginLayout.setVisibility(View.VISIBLE);
                mLoginSucces.setVisibility(View.GONE);
                break;
            case R.id.decode://生成二维�?
                mDecodeImg.setImageBitmap(EncodingUtils.createQRCode("Hello World !", 400, 400, BitmapFactory.decodeResource(getResources(), R.mipmap.aa)));
                break;
            case R.id.encode:
                startActivityForResult(new Intent(getActivity(), CaptureActivity.class), 1000);
                break;
            case R.id.play:
                startActivity(new Intent(getActivity(), MyVideoPlayerActivity.class));
                break;
            case R.id.record:
                startActivity(new Intent(getActivity(), MyLiveRecordActivity.class));
                break;
            case R.id.testRecord:
                startActivity(new Intent(getActivity(),RecoreCamera2Activity.class));
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==1000){
            if (resultCode==getActivity().RESULT_OK){
                String result=data.getStringExtra("result");
                mEncode.setText(result);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 登录用户中心
     *
     * @param userName
     * @param passWord
     */
    private void netForLogin(final String userName, String passWord) {

        RequestParams params = new RequestParams(UrlUtils.LoginUrl);
        params.addQueryStringParameter("platform", "1");
        params.addQueryStringParameter("channel", "1");
        params.addQueryStringParameter("act", "login");
        params.addQueryStringParameter("username", "blog");
        params.addQueryStringParameter("password", "123456");
        LogUtils.i(UrlUtils.LoginUrl + "?platform=1&channel=1&act=login&username=" + userName + "&password=" + passWord);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.i("接口返回数据==》�??" + result);
                UserInfo userInfo = parseData(result);
                if (userInfo != null) {
                    mSpu.saveUserInfo(userInfo);
                    mUserInfo = userInfo;
                    hideKeyBoard(mPassWord);
                    showUserMsg(userInfo);
                    PARAM.connect(getActivity(), userInfo.getToken());
                } else {
                    Toast.makeText(getActivity(), "数据解析异常!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(getActivity(), "登录失败!", Toast.LENGTH_SHORT).show();
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
     * 展现用户信息
     *
     * @param userInfo
     */
    private void showUserMsg(UserInfo userInfo) {
        mLoginLayout.setVisibility(View.GONE);
        mPassWord.setText("");
        mUserName.setText("");
        mLoginSucces.setVisibility(View.VISIBLE);
        x.image().bind(mUserHead, userInfo.getPicUrl());
        mUserNickName.setText(userInfo.getNickName());
        mUserPhone.setText(userInfo.getMobile());
        mUserGold.setText(userInfo.getGolden());
    }

    /**
     * 解析数据
     *
     * @param msg
     */
    private UserInfo parseData(String msg) {
        UserInfo userInfo = null;
        try {
            JSONObject object = new JSONObject(msg);
            String str = object.getString("flag");
            if ("111".equals(str)) {
                JSONObject data = object.optJSONObject("data");
                String userId = data.optString("userid");
                String nickName = data.optString("nickname");
                String account = data.optString("username");
                String money = data.optString("money");
                String mobile = data.optString("mobile");
                String headUrl = data.optString("headurl");

                userInfo = new UserInfo();
                userInfo.setUserId(userId);
                userInfo.setNickName(nickName);
                userInfo.setUserAccount(account);
                userInfo.setGolden(money);
                userInfo.setMobile(mobile);
                userInfo.setPicUrl(headUrl);
                userInfo.setPassword(mPassWord.getText().toString().trim());
                if (userId.equals("348392"))
                    userInfo.setToken("kWilRo1NjQ/pUg9zXWrecMktTNuNHS1Zkx2bx0zt4HhoPPT8F61vnYPPueHijQyPdrFGBx5FVdACz1PgMCoxog==");
                else
                    userInfo.setToken("QBToKar/571CwaPdE4yZlk762pC/b3PFlzYazjzFb0WNfl6jGXiHCItM88q8Sul6Sc/BIV/ptkcee0n1WzQRaw==");
                LogUtils.i(userInfo.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userInfo;
    }


    /**
     * 隐藏键盘
     *
     * @param v
     */
    public void hideKeyBoard(View v) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnUserFragmentInteractionListener {
        // TODO: Update argument type and name
        void onUserFragmentInteraction(Object object);
    }


}
