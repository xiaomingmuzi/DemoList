package com.lixm.chat.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lixm.chat.R;

import org.xutils.common.util.LogUtils;

import java.util.regex.Pattern;

/**
 * User: LXM
 * Date: 2016-08-17
 * Time: 09:43
 * Detail:自定义底部聊天输入框
 */
public class InputTextMsgLayout extends LinearLayout {

    private TextView confirmBtn;
    private EditText messageTextView;
    private Context mContext;
//    private LiveHelper mLiveControlHelper;
    private InputMethodManager imm;
    private int mLastDiff = 0;
    private final String reg = "[`~@#$%^&*()-_+=|{}':;,/.<>￥…（）—【】‘；：”“’。，、]";
    private Pattern pattern = Pattern.compile(reg);
//    private SharedPreferencesUtil mSpu;

    public InputTextMsgLayout(Context context) {
        super(context);
        init(context);
    }

    public InputTextMsgLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public InputTextMsgLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 初始化界面
     *
     * @param context
     */
    private void init(Context context) {
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.input_text_dialog, this);
//        mSpu = new SharedPreferencesUtil(context);
//        LogUtils.i("是否有未发送成功的文字----》" + mSpu.getLiveMsg());
        messageTextView = (EditText) findViewById(R.id.input_message);
        messageTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                mSpu.saveLiveMsg(s.toString().trim());
            }
        });
        confirmBtn = (TextView) findViewById(R.id.confrim_btn);
        imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        confirmBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtils.d("onClick enter->");
                if (messageTextView.getText().toString().trim().length() > 0) {
//                    sendText("" + messageTextView.getText());
                    show(GONE);
                } else {
//                    FinancialToast.show(mContext, "请输入内容", Toast.LENGTH_LONG);
                }
            }
        });
        messageTextView.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                LogUtils.d("XIAO======onKey->" + v + "," + keyCode + "," + event);
                if (event.getAction() != KeyEvent.ACTION_UP) {   // 忽略其它事件
                    return false;
                }

                switch (keyCode) {
                    case KeyEvent.KEYCODE_ENTER:
                        if (messageTextView.getText().toString().trim().length() > 0) {
                            show(GONE);
//                            sendText("" + messageTextView.getText());
                        } else {
//                            FinancialToast.show(mContext, "请输入内容", Toast.LENGTH_LONG);
                        }
                        return true;
                    default:
                        return false;
                }
            }
        });

        final LinearLayout rldlgview = (LinearLayout) findViewById(R.id.rl_inputdlg_view);
        rldlgview.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                //获取当前界面可视部分
                ((Activity) mContext).getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
                //获取屏幕的高度
                int screenHeight = ((Activity) mContext).getWindow().getDecorView().getRootView().getHeight();
                //此处就是用来获取键盘的高度的， 在键盘没有弹出的时候 此高度为0 键盘弹出的时候为一个正数
                int heightDifference = screenHeight - r.bottom;

                if (heightDifference <= 0 && mLastDiff > 0) {
//                    imm.hideSoftInputFromWindow(messageTextView.getWindowToken(), 0);
                   show(GONE);
                }
                LogUtils.d(heightDifference + "/" + mLastDiff);
                mLastDiff = heightDifference;
            }
        });
        rldlgview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                show(GONE);
            }
        });
    }

//    public void setmLiveControlHelper(LiveHelper mLiveControlHelper) {
//        this.mLiveControlHelper = mLiveControlHelper;
//    }

    /**
     * add message text
     */
    private void setMessageText(String strInfo) {
        messageTextView.setText(strInfo);
        messageTextView.setSelection(strInfo.length());
    }

    /**
     * 控件显示还是隐藏
     *
     * @param flag 参数使用View的VISIBLE和GONE
     */
    public void show(int flag) {
        switch (flag) {
            case VISIBLE:
//                if (!TextUtils.isEmpty(mSpu.getLiveMsg())) {
//                    setMessageText(mSpu.getLiveMsg());
//                }
                setVisibility(VISIBLE);
                messageTextView.requestFocus();
                imm.showSoftInput(messageTextView, InputMethodManager.SHOW_FORCED);
//                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                break;
            case GONE:
                imm.hideSoftInputFromWindow(messageTextView.getWindowToken(), 0);
//                setVisibility(GONE);
                break;
        }
    }

    /**
     * 发送消息
     *
     * @param msg
     */
//    private void sendText(String msg) {
////        mSpu.saveLiveMsg("");
//        messageTextView.setText("");
//        if (msg.length() == 0)
//            return;
//        try {
//            byte[] byte_num = msg.getBytes("utf8");
//            if (byte_num.length > 160) {
////                FinancialToast.show(mContext, mContext.getResources().getString(R.string.live_msg_too_long), Toast.LENGTH_SHORT);
//                return;
//            }
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//            return;
//        }
//        TIMMessage Nmsg = new TIMMessage();
//        TIMTextElem elem = new TIMTextElem();
//        elem.setText(msg);
//        if (Nmsg.addElement(elem) != 0) {
//            return;
//        }
//        LogUtils.e("发送消息======" + Nmsg.toString());
//        mLiveControlHelper.sendGroupText(Nmsg);
//    }

}
