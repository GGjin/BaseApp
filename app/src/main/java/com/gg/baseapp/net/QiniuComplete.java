package com.gg.baseapp.net;

import com.qiniu.android.http.ResponseInfo;

import org.json.JSONObject;

/**
 * Created by GG on 2017/6/1.
 */

public interface QiniuComplete {

    public void Compplete(String key, ResponseInfo info, JSONObject response, String tag);
}

