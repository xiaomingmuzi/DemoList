package com.lixm.chat.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lixm.chat.R;
import com.lixm.chat.adapter.MsgRecyclerAdapter;
import com.lixm.chat.bean.ReceiverBean;
import com.lixm.chat.bean.UserInfo;
import com.lixm.chat.database.DBHelper;
import com.lixm.chat.util.SharedPrefrenceUtils;

import org.xutils.DbManager;
import org.xutils.common.util.KeyValue;
import org.xutils.common.util.LogUtils;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnMsgFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MsgFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MsgFragment extends BaseFragment {
    private Context mContext;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnMsgFragmentInteractionListener mListener;
    private ArrayList<ReceiverBean> msgBeans;
    private MsgRecyclerAdapter msgRecyclerAdapter;
    private View mRoot;
    private RecyclerView mRecyclerView;
    private DbManager mDbManager;
    private UserInfo mUserInfo;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("ReceiveMsg")) {
                ReceiverBean receiverBean = (ReceiverBean) intent.getParcelableExtra("ReceiverBean");
                try {
                    ReceiverBean msgDatabaseBean = mDbManager.findById(ReceiverBean.class, receiverBean.getSenderUserId());
                    if (msgDatabaseBean != null) {
                        mDbManager.update(ReceiverBean.class, WhereBuilder.b("senderUserId", "=", receiverBean.getSenderUserId()).and("toUserId", "=", receiverBean.getToUserId()),
                                new KeyValue[]{new KeyValue("messageId", receiverBean.getMessageId()), new KeyValue("content", receiverBean.getContent()), new KeyValue("receiveTime", receiverBean.getReceiveTime())});
                    } else {
                        mDbManager.save(receiverBean);
                    }
                    msgBeans = (ArrayList<ReceiverBean>) mDbManager.selector(ReceiverBean.class)
                            .where("senderUserId", "=", receiverBean.getSenderUserId())
                            .and("toUserId", "=", receiverBean.getToUserId())
                            .orderBy("id")
                            .limit(10).findAll();
                } catch (DbException e) {
                    e.printStackTrace();
                    LogUtils.d(e.toString());
                }
                msgRecyclerAdapter.bindData(msgBeans);
                msgRecyclerAdapter.notifyDataSetChanged();
            }
        }
    };

    public MsgFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MsgFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MsgFragment newInstance(String param1, String param2) {
        MsgFragment fragment = new MsgFragment();
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
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.fragment_msg, container, false);
        mDbManager = DBHelper.getDbManager();
        mUserInfo = (new SharedPrefrenceUtils(getActivity())).getUserInfo();
        msgBeans = new ArrayList<>();
        try {
            ArrayList<ReceiverBean> databaseBeans = (ArrayList) mDbManager.findAll(ReceiverBean.class);
            if (databaseBeans != null && databaseBeans.size() != 0)
                msgBeans.addAll(databaseBeans);
        } catch (DbException e) {
            e.printStackTrace();
        }
        mRecyclerView = (RecyclerView) mRoot.findViewById(R.id.msg_recycleView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(msgRecyclerAdapter = new MsgRecyclerAdapter(mContext, msgBeans));
        IntentFilter intentFilter = new IntentFilter("ReceiveMsg");
        getActivity().registerReceiver(mReceiver, intentFilter);
        return mRoot;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onMsgFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMsgFragmentInteractionListener) {
            mListener = (OnMsgFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMsgFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (mReceiver != null) {
                getActivity().unregisterReceiver(mReceiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnMsgFragmentInteractionListener {
        // TODO: Update argument type and name
        void onMsgFragmentInteraction(Object object);
    }
}
