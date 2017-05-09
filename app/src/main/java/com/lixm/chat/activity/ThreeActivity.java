package com.lixm.chat.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.lixm.chat.R;

/**
 * User: LXM
 * Date: 2016-08-18
 * Time: 16:15
 * Detail:
 */
public class ThreeActivity extends Activity {
    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                        | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_trade_plan_detail);
//        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        ListView list = (ListView) findViewById(R.id.im_msg_listview);
//        ArrayList<String> arrayList = new ArrayList<>();
//        arrayList.add("管理员：哈哈哈哈哈");
//        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_2, android.R.id.text1, arrayList);
//        list.setAdapter(arrayAdapter);
//        final InputTextMsgLayout inputTextMsgLayout = (InputTextMsgLayout) findViewById(R.id.inputTxt);
//        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                inputTextMsgLayout.setVisibility(View.VISIBLE);
//                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//            }
//        });
    }

    public void showKeyBoard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
