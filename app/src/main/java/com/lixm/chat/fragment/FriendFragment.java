package com.lixm.chat.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;

import com.lixm.chat.R;
import com.lixm.chat.adapter.FriendAdapter;
import com.lixm.chat.adapter.FriendRecyclerAdapter;
import com.lixm.chat.bean.ReceiverBean;
import com.lixm.chat.view.SyLinearLayoutManager;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFriendFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FriendFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@ContentView(R.layout.fragment_friend)
public class FriendFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFriendFragmentInteractionListener mListener;
    private Context mContext;
    private FriendRecyclerAdapter mFriRecyclerAdapter;
    private FriendAdapter mFriendAdapter;
    private ArrayList<ReceiverBean> mList;

    @ViewInject(R.id.friend_RV)
    private RecyclerView mRecyclerView;
    @ViewInject(R.id.friend_LV)
    private ListView mListView;

    public FriendFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FriendFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FriendFragment newInstance(String param1, String param2) {
        FriendFragment fragment = new FriendFragment();
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
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext=getActivity();
        mRecyclerView.setLayoutManager(new SyLinearLayoutManager(mContext));
        mList=new ArrayList<>();
        ReceiverBean receiverBean1=new ReceiverBean();

        receiverBean1.setMsgType(1);
        receiverBean1.setContent("文字，文字");
        receiverBean1.setSenderUserId("345981");
        receiverBean1.setUserHead("http://images-shichai.test.cnfol.com/userHead/3459811460513333.jpg");
        receiverBean1.setUserName("666");
        mList.add(receiverBean1);

        ReceiverBean receiverBean2=new ReceiverBean();
        receiverBean2.setMsgType(2);
        receiverBean2.setContent("图文");
        receiverBean2.setSenderUserId("345981");
        receiverBean2.setUserHead("http://images-shichai.test.cnfol.com/userHead/3459811460513333.jpg");
        receiverBean2.setThumUri("http://img.firefoxchina.cn/2016/03/4/201603021455240.jpg");
        receiverBean2.setUserName("666");
        mList.add(receiverBean2);

        ReceiverBean receiverBean3=new ReceiverBean();
        receiverBean3.setMsgType(3);
        receiverBean3.setContent("视频司法发生的发生的发生东方卫视vvxvdhhdghfg");
        receiverBean3.setSenderUserId("345981");
        receiverBean3.setUserHead("http://images-shichai.test.cnfol.com/userHead/3459811460513333.jpg");
        receiverBean3.setThumUri("http://img.firefoxchina.cn/2016/04/8/201604131759310.jpg");
        receiverBean3.setUserName("666");
        mList.add(receiverBean3);

        ReceiverBean receiverBean4=new ReceiverBean();
        receiverBean4.setMsgType(3);
        receiverBean4.setContent("视频我发的说点击事件飞洛杉矶的份上就放假了设计费蓝山咖啡四谛法上发生地方 ");
        receiverBean4.setSenderUserId("348392");
        receiverBean4.setUserHead("http://images-shichai.test.cnfol.com/userHead/3483921459839665.jpg");
        receiverBean4.setThumUri("http://img.firefoxchina.cn/2015/10/7/201510261136480.jpg");
        receiverBean4.setUserName("blog");
        mList.add(receiverBean4);

        mRecyclerView.setAdapter(mFriRecyclerAdapter = new FriendRecyclerAdapter(mContext, mList));
        mListView.setAdapter(mFriendAdapter=new FriendAdapter(mContext,mList));
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFriendFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFriendFragmentInteractionListener) {
            mListener = (OnFriendFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFriendFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface OnFriendFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFriendFragmentInteraction(Object object);
    }
}
