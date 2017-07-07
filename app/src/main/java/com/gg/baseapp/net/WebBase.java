package com.gg.baseapp.net;

import android.graphics.Bitmap;

import com.gg.baseapp.utils.Log;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.Response;

/**
 * Created by GG on 2017/5/22.
 */

public class WebBase {

    /**
     * 打印成功请求信息
     *
     * @param data
     * @param call
     * @param response
     * @param <T>
     */

    public <T> void handleResponse(T data, Call call, Response response) {
        StringBuilder sb;
        if (call != null) {
            Log.e("请求成功  请求方式：" + call.request().method() + "\n" + "url：" + call.request().url());

            Headers requestHeadersString = call.request().headers();
            Set<String> requestNames = requestHeadersString.names();
            sb = new StringBuilder();
            for (String name : requestNames) {
                sb.append(name).append(" ： ").append(requestHeadersString.get(name)).append("\n");
            }
            Log.e(sb.toString());
        }
        if (data != null) {
            if (data instanceof String) {
                Log.printJSON((String) data);
            } else if (data instanceof List) {
                sb = new StringBuilder();
                List list = (List) data;
                for (Object obj : list) {
                    sb.append(obj.toString()).append("\n");
                }
                Log.e(sb.toString());
            } else if (data instanceof Set) {
                sb = new StringBuilder();
                Set set = (Set) data;
                for (Object obj : set) {
                    sb.append(obj.toString()).append("\n");
                }
                Log.e(sb.toString());
            } else if (data instanceof Map) {
                sb = new StringBuilder();
                Map map = (Map) data;
                Set keySet = map.keySet();
                for (Object key : keySet) {
                    sb.append(key.toString()).append(" ： ").append(map.get(key)).append("\n");
                }
                Log.e(sb.toString());
            } else if (data instanceof File) {
                File file = (File) data;
                Log.e("数据内容即为文件内容\n下载文件路径：" + file.getAbsolutePath());
            } else if (data instanceof Bitmap) {
                Log.e("图片的内容即为数据");
            } else {
                Log.printJSON(data.toString());
            }
        }

        if (response != null) {
            Headers responseHeadersString = response.headers();
            Set<String> names = responseHeadersString.names();
            sb = new StringBuilder();
            sb.append("url ： ").append(response.request().url()).append("\n\n");
            sb.append("stateCode ： ").append(response.code()).append("\n");
            for (String name : names) {
                sb.append(name).append(" ： ").append(responseHeadersString.get(name)).append("\n");
            }
            Log.e(sb.toString());
        }
    }

    /**
     * 打印错误请求信息
     *
     * @param call
     * @param response
     */

    public void handleError(Call call, Response response) {
        StringBuilder sb;
        if (call != null) {
            Log.e("请求失败  请求方式：" + call.request().method() + "\n" + "url：" + call.request().url());

            Headers requestHeadersString = call.request().headers();
            Set<String> requestNames = requestHeadersString.names();
            sb = new StringBuilder();
            for (String name : requestNames) {
                sb.append(name).append(" ： ").append(requestHeadersString.get(name)).append("\n");
            }
            Log.e(sb.toString());
        }

        if (response != null) {
            Headers responseHeadersString = response.headers();
            Set<String> names = responseHeadersString.names();
            sb = new StringBuilder();
            sb.append("stateCode ： ").append(response.code()).append("\n");
            for (String name : names) {
                sb.append(name).append(" ： ").append(responseHeadersString.get(name)).append("\n");
            }
            Log.e(sb.toString());
        }
    }
}
