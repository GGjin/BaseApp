package com.gg.baseapp.net;

import com.gg.baseapp.bean.GingResponse;
import com.gg.baseapp.bean.SimpleResponse;
import com.gg.baseapp.utils.AppManager;
import com.gg.baseapp.utils.GsonUtil;
import com.gg.baseapp.utils.JsonUtils;
import com.gg.baseapp.utils.Log;
import com.gg.baseapp.utils.ToastUtil;
import com.google.gson.stream.JsonReader;
import com.lzy.okgo.callback.AbsCallback;

import org.json.JSONObject;

import java.io.StringReader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by GG on 2017/5/19.
 */

public abstract class JsonCallBack<T> extends AbsCallback<T> {
    @Override
    public void onSuccess(T t, Call call, Response response) {

    }

    @Override
    public void onError(Call call, Response response, Exception e) {
        super.onError(call, response, e);
        ToastUtil.showLong(AppManager.getAppManager().currentActivity(), e.getMessage());
    }

    @Override
    public T convertSuccess(Response response) throws Exception {

        // 重要的事情说三遍，不同的业务，这里的代码逻辑都不一样，如果你不修改，那么基本不可用
        // 重要的事情说三遍，不同的业务，这里的代码逻辑都不一样，如果你不修改，那么基本不可用
        // 重要的事情说三遍，不同的业务，这里的代码逻辑都不一样，如果你不修改，那么基本不可用

        //以下代码是通过泛型解析实际参数,泛型必须传
        //这里为了方便理解，假如请求的代码按照上述注释文档中的请求来写，那么下面分别得到是

        //com.lzy.demo.callback.DialogCallback<com.lzy.demo.model.LzyResponse<com.lzy.demo.model.ServerModel>> 得到类的泛型，包括了泛型参数
        Type genType = getClass().getGenericSuperclass();
        //从上述的类中取出真实的泛型参数，有些类可能有多个泛型，所以是数值
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        //我们的示例代码中，只有一个泛型，所以取出第一个，得到如下结果
        //com.lzy.demo.model.LzyResponse<com.lzy.demo.model.ServerModel>
        Type type = params[0];

        // 这里这么写的原因是，我们需要保证上面我解析到的type泛型，仍然还具有一层参数化的泛型，也就是两层泛型
        // 如果你不喜欢这么写，不喜欢传递两层泛型，那么以下两行代码不用写，并且javabean按照第一种方式定义就可以实现
        if (!(type instanceof ParameterizedType)) throw new IllegalStateException("没有填写泛型参数");
        //如果确实还有泛型，那么我们需要取出真实的泛型，得到如下结果
        //class com.lzy.demo.model.LzyResponse
        //此时，rawType的类型实际上是 class，但 Class 实现了 Type 接口，所以我们用 Type 接收没有问题
        Type rawType = ((ParameterizedType) type).getRawType();
        //这里获取最终内部泛型的类型 com.lzy.demo.model.ServerModel
        Type typeArgument = ((ParameterizedType) type).getActualTypeArguments()[0];

        JSONObject jsonObject = new JSONObject(response.body().string());
        JsonUtils.cleanDirtyJsonObject(jsonObject);
        //这里我们既然都已经拿到了泛型的真实类型，即对应的 class ，那么当然可以开始解析数据了，我们采用 Gson 解析
        //以下代码是根据泛型解析数据，返回对象，返回的对象自动以参数的形式传递到 onSuccess 中，可以直接使用
//        JsonReader jsonReader = new JsonReader(response.body().charStream());
        JsonReader jsonReader= new JsonReader(new StringReader(jsonObject.toString()));
        if (typeArgument == Void.class) {
            //无数据类型,表示没有data数据的情况（以  new DialogCallback<LzyResponse<Void>>(this)  以这种形式传递的泛型)
            SimpleResponse simpleResponse = GsonUtil.fromJson(jsonReader, SimpleResponse.class);
            response.close();
            //noinspection unchecked
            return (T) simpleResponse.toGingResponse();
        } else if (rawType == GingResponse.class) {
            //有数据类型，表示有data
            GingResponse gingResponse = GsonUtil.fromJson(jsonReader, type);
            response.close();
            int status = gingResponse.Status;
            //这里的0是以下意思
            //一般来说服务器会和客户端约定一个数表示成功，其余的表示失败，这里根据实际情况修改
            if (status == 0) {
                //noinspection unchecked
                return (T) gingResponse;
            } else {
                Log.e("错误代码：" + status + "，错误信息：" + gingResponse.Msg);
                throw new IllegalStateException(gingResponse.Msg);
            }
        } else {
            response.close();
            throw new IllegalStateException("基类错误无法解析!");
        }
    }
}
