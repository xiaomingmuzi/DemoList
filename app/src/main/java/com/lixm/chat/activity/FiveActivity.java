package com.lixm.chat.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lixm.chat.R;
import com.lixm.chat.util.MediaRecorderUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_five)
public class FiveActivity extends Activity implements View.OnClickListener {
    @ViewInject(R.id.button3)
    private Button mBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        mBtn.setOnClickListener(this);
        MediaRecorderUtils.getInstence(null).setImageType(1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button3:
                if (mBtn.getText().toString().equals("Start")) {
                    MediaRecorderUtils.getInstence(null).MediaRecorderStart();
                    mBtn.setText("Recording");
                } else {
                    MediaRecorderUtils.getInstence(null).MediaRecorderStop();
                    mBtn.setText("Start");
                }
                break;
        }
    }
}
