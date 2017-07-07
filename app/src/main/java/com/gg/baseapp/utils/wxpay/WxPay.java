package com.gg.baseapp.utils.wxpay;

import android.app.Activity;

import com.gg.baseapp.bean.NameValuePair;
import com.gg.baseapp.utils.Log;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by GG on 2017/6/1.
 */

public class WxPay {


    private PayReq request;
    private Activity context;
    IWXAPI msgApi;

    public WxPay(Activity context, String appId, String partnerId, String prepayId) {
        this.context = context;

        this.request = new PayReq();
        this.request.appId = appId;
        this.request.partnerId = partnerId;
        this.request.prepayId = prepayId;
        this.request.packageValue = "Sign=WXPay";
        request.nonceStr = genNonceStr();
        request.timeStamp = String.valueOf(genTimeStamp());


        List<NameValuePair> signParams = new LinkedList<>();
        signParams.add(new NameValuePair("appid", request.appId));
        signParams.add(new NameValuePair("noncestr", request.nonceStr));
        signParams.add(new NameValuePair("package", request.packageValue));
        signParams.add(new NameValuePair("partnerid", request.partnerId));
        signParams.add(new NameValuePair("prepayid", request.prepayId));
        signParams.add(new NameValuePair("timestamp", request.timeStamp));

        request.sign = genPackageSign(signParams);
    }


    private String genNonceStr() {
        Random random = new Random();
        return MD5Util.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    private String genPackageSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
//        sb.append(Constants.API_KEY);


        String packageSign = MD5Util.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        Log.e("orion", packageSign);
        return packageSign;
    }


    public void start() {
        msgApi = WXAPIFactory.createWXAPI(context, null);
        msgApi.registerApp(Constants.APP_ID);
        msgApi.sendReq(request);
    }
}
