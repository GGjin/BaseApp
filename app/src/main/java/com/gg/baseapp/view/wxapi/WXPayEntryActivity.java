package com.azging.ging.view.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.gg.baseapp.utils.Log;
import com.gg.baseapp.utils.constants.LocalBroadcastHelper;
import com.gg.baseapp.utils.wxpay.Constants;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);

        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.d(TAG, "wxPayFinish, errCode = " + resp.errCode);
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (resp.errCode == 0) {//支付成功
                MobclickAgent.onEvent(getApplication(), "Payment_Succeed");
                LocalBroadcastHelper.notifyWXPayResult(localBroadcastManager, true);
            } else {
                MobclickAgent.onEvent(getApplication(), "Payment_Failed");
                LocalBroadcastHelper.notifyWXPayResult(localBroadcastManager, false);
            }
            finish();
        }
    }
}