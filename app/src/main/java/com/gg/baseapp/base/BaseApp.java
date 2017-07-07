package com.gg.baseapp.base;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.gg.baseapp.BuildConfig;
import com.gg.baseapp.R;
import com.gg.baseapp.bean.ActivityBean;
import com.gg.baseapp.net.NetChangeObserver;
import com.gg.baseapp.net.NetWorkUtil;
import com.gg.baseapp.net.NetworkStateReceiver;
import com.gg.baseapp.utils.AppManager;
import com.gg.baseapp.utils.Log;
import com.gg.baseapp.utils.PrefConstants;
import com.gg.baseapp.utils.SharedPreferencesHelper;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.store.PersistentCookieStore;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.util.logging.Level;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * 项目名称：ProjectFunction
 * <p>
 * 1>初始化全局配置,第三方的sdk
 * 2>初始化第三方的SDK
 * 3>初始化通用框架
 * 全局异常处理
 * 友盟统计有全局异常的处理
 */

public class BaseApp extends Application implements Application.ActivityLifecycleCallbacks {

    private static final String KEY_PREF_LOCATION_SUBMIT = "location_time_tag";


    public static BaseApp app;
    public static AMapLocation currentLocation;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    currentLocation = aMapLocation;
                    Log.e("Lat====" + currentLocation.getLatitude() + "-------    Lon====" + currentLocation.getLongitude());

                    long lastSubmit = SharedPreferencesHelper.getInstance(getApplicationContext()).getLongValue(KEY_PREF_LOCATION_SUBMIT);
                    if (System.currentTimeMillis() - lastSubmit > PrefConstants.LOCATION_SUBMIT_INTERVAL) {
//                        if (TextUtils.isEmpty(clientId)) { //clientId is not ready
//                            return;
//                        }
                        Log.e("app set user config");
//                        webApp.setUserConfig(clientId, new WebResultCallback() {
//                            @Override
//                            public void resultGot(int rescode, JSONObject dataObj, String msg) throws JSONException {
                        SharedPreferencesHelper.getInstance(getApplicationContext()).putLongValue(KEY_PREF_LOCATION_SUBMIT, System.currentTimeMillis());
//                                if (rescode == 0) {
//                                    LocalBroadcastHelper.notifyWebLocNameUpdated(localBroadcastManager, dataObj.toString());
//                                }
//                            }
//                        });
                    }
//可在其中解析amapLocation获取相应内容。
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }
        }
    };


    private NetChangeObserver observer = new NetChangeObserver() {

        @Override
        public void onConnect(NetWorkUtil.NetType type) {
            super.onConnect(type);

            //表示已连接
            switch (type) {
                case NETWORK_WIFI:
                    //表示wifi
                    break;
                default:
                    Toast.makeText(BaseApp.this, "2/3/4G网络已连接", Toast.LENGTH_SHORT).show();
                    break;
            }

        }

        @Override
        public void onDisConnect() {
            super.onDisConnect();
            //表示网络已断开
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;


        //init umeng config
        if (BuildConfig.DEBUG) {
            MobclickAgent.setDebugMode(true);
            MobclickAgent.setCatchUncaughtExceptions(false);
        } else {
            MobclickAgent.setDebugMode(false);
            MobclickAgent.setCatchUncaughtExceptions(true);
        }
        MobclickAgent.openActivityDurationTrack(false);
//		MobclickAgent.updateOnlineConfig(this);

        UMShareAPI.get(this);
        Config.isJumptoAppStore = true;


        Log.i("", "onCreate: ");
        initLocation();

        initNetState();

        initOkGo();

        registerActivityLifecycleCallbacks(this);

    }


    {
        PlatformConfig.setWeixin("wx134589ca25302f9d", "dd644317c7e778656b49619564319435");
    }


    private void initOkGo() {

        //必须调用初始化
        OkGo.init(this);

        //以下设置的所有参数是全局参数,同样的参数可以在请求的时候再设置一遍,那么对于该请求来讲,请求中的参数会覆盖全局参数
        //好处是全局参数统一,特定请求可以特别定制参数
        try {
            //以下都不是必须的，根据需要自行选择,一般来说只需要 debug,缓存相关,cookie相关的 就可以了
            OkGo.getInstance()

                    // 打开该调试开关,打印级别INFO,并不是异常,是为了显眼,不需要就不要加入该行
                    // 最后的true表示是否打印okgo的内部异常，一般打开方便调试错误
                    .debug("OkGo", Level.INFO, true)

                    //如果使用默认的 60秒,以下三行也不需要传
                    .setConnectTimeout(OkGo.DEFAULT_MILLISECONDS)  //全局的连接超时时间
                    .setReadTimeOut(OkGo.DEFAULT_MILLISECONDS)     //全局的读取超时时间
                    .setWriteTimeOut(OkGo.DEFAULT_MILLISECONDS)    //全局的写入超时时间

                    //可以全局统一设置缓存模式,默认是不使用缓存,可以不传,具体其他模式看 github 介绍 https://github.com/jeasonlzy/
                    .setCacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)

                    //可以全局统一设置缓存时间,默认永不过期,具体使用方法看 github 介绍
                    .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)

                    //可以全局统一设置超时重连次数,默认为三次,那么最差的情况会请求4次(一次原始请求,三次重连请求),不需要可以设置为0
                    .setRetryCount(3)

                    //如果不想让框架管理cookie（或者叫session的保持）,以下不需要
//              .setCookieStore(new MemoryCookieStore())            //cookie使用内存缓存（app退出后，cookie消失）
                    .setCookieStore(new PersistentCookieStore())        //cookie持久化存储，如果cookie不过期，则一直有效

                    //可以设置https的证书,以下几种方案根据需要自己设置
                    .setCertificates();                             //方法一：信任所有证书,不安全有风险
//              .setCertificates(new SafeTrustManager())            //方法二：自定义信任规则，校验服务端证书
//              .setCertificates(getAssets().open("srca.cer"))      //方法三：使用预埋证书，校验服务端证书（自签名证书）
//              //方法四：使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
//               .setCertificates(getAssets().open("xxx.bks"), "123456", getAssets().open("yyy.cer"))//

            //配置https的域名匹配规则，详细看demo的初始化介绍，不需要就不要加入，使用不当会导致https握手失败
//               .setHostnameVerifier(new SafeHostnameVerifier())

            //可以添加全局拦截器，不需要就不要加入，错误写法直接导致任何回调不执行
//                .addInterceptor(new Interceptor() {
//                    @Override
//                    public Response intercept(Chain chain) throws IOException {
//                        return chain.proceed(chain.request());
//                    }
//                })

            //这两行同上，不需要就不要加入
//                    .addCommonHeaders(headers); //设置全局公共头
//                    .addCommonParams(params);   //设置全局公共参数

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initNetState() {
        //动态注册网络监听
        NetworkStateReceiver.registerNetworkStateReceiver(this);

        //注册网络变化的观察者
        NetworkStateReceiver.registerObserver(observer);
    }


    /**
     * 获取Location通过LocationManger获取！
     */
    public void initLocation() {

        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);


        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();

        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);

        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);

        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }


    /**
     * 程序结束的位置 注销监听
     */
    public void unRegisterNetworkStateReceiver() {
        NetworkStateReceiver.unRegisterNetworkStateReceiver(this);
        if (observer != null) {
            NetworkStateReceiver.removeRegisterObserver(observer);
        }

    }


    @Override
    public void onActivityCreated(final Activity activity, Bundle savedInstanceState) {
        if (activity instanceof IActivity) {
            activity.setContentView(((IActivity) activity).initView());
            ActivityBean bean = new ActivityBean();
            Unbinder unbinder = ButterKnife.bind(activity);
            bean.setUnbinder(unbinder);
            activity.getIntent().putExtra("ActivityBean", bean);
            ((IActivity) activity).initData();
        }


        if (activity.findViewById(R.id.toolbar) != null) {
            if (activity instanceof AppCompatActivity) {
                ((AppCompatActivity) activity).setSupportActionBar((Toolbar) activity.findViewById(R.id.toolbar));
                ((AppCompatActivity) activity).getSupportActionBar().setDisplayShowTitleEnabled(false);
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    activity.setActionBar((android.widget.Toolbar) activity.findViewById(R.id.toolbar));
                    activity.getActionBar().setDisplayShowTitleEnabled(false);
                }
            }
        }
        if (activity.findViewById(R.id.toolbar_title) != null) {
            ((TextView) activity.findViewById(R.id.toolbar_title)).setText(activity.getTitle());
        }
        if (activity.findViewById(R.id.toolbar_back) != null) {
            activity.findViewById(R.id.toolbar_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.onBackPressed();
                }
            });
        }

        AppManager.getAppManager().addActivity(activity);

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        MobclickAgent.onPageStart(activity.getClass().getName());
        MobclickAgent.onResume(activity);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        MobclickAgent.onPageEnd(activity.getClass().getName());
        MobclickAgent.onPause(activity);
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if (activity instanceof IActivity) {
            ActivityBean bean = activity.getIntent().getParcelableExtra("ActivityBean");
            bean.getUnbinder().unbind();
        }
    }


}
