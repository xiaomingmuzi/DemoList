package com.lixm.chat.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.lixm.chat.activity.MainActivity;
import com.lixm.chat.application.APP;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    //    boolean sendReq(BaseReq req);
//    sendReq是第三方app主动发送消息给微信，发送完成之后会切回到第三方app界面。
//    boolean sendResp(BaseResp resp);
//    sendResp是微信向第三方app请求数据，第三方app回应数据之后会切回到微信界面。
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textView = new TextView(this);
        setContentView(textView);
        textView.setText("微信回调界面");
        MainActivity.api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        MainActivity.api.handleIntent(intent, this);
        finish();
    }

    @Override
    public void onReq(BaseReq baseReq) {
        textView.append("\nonReq(BaseReq baseReq)");
        finish();
    }

    @Override
    public void onResp(BaseResp baseResp) {
        textView.append("\nonResp(BaseResp baseResp)");
        //3.获取用户授权信息
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK://用户同意
                SendAuth.Resp sendResp = (SendAuth.Resp) baseResp;
                APP.WX_CODE = sendResp.code;
                Toast.makeText(this, "成功获取用户授权，Code为：" + sendResp.code, Toast.LENGTH_SHORT).show();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED://用户拒绝授权
                Toast.makeText(this, "用户拒绝授权，登录失败!", Toast.LENGTH_SHORT).show();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL://用户取消
                Toast.makeText(this, "用户取消授权，登录失败!", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}
