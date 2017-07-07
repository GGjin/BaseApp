package com.gg.baseapp.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.gg.baseapp.R;
import com.gg.baseapp.base.BaseMainActivity;
import com.gg.baseapp.base.IActivity;
import com.gg.baseapp.utils.AppManager;
import com.gg.baseapp.utils.PermissionsChecker;
import com.gg.baseapp.utils.PhoneUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpHeaders;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class MainActivity extends BaseMainActivity implements IActivity {
    private static final int REQUEST_CODE = 0; // 请求码
    @BindView(R.id.main_tab) TabLayout mainTab;
    @BindView(R.id.main_viewpager) ViewPager mainViewpager;
    private PermissionsChecker mPermissionsChecker; // 权限检测器

    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    private String[] titles;
    private FragmentManager manager;
    private MainViewPagerAdapter viewPagerAdapter;
    private List<Fragment> fragments = new ArrayList<>();

    @Override
    public int initView() {
        return R.layout.activity_main;
    }

    @Override
    public void initData() {
        mPermissionsChecker = new PermissionsChecker(this);
//
        // 缺少权限时, 进入权限配置页面
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        } else {
            initCookie();
        }


        fragments.add(MainFragment.startFragment("A"));
        fragments.add(MainFragment.startFragment("B"));
        fragments.add(MainFragment.startFragment("C"));


        titles = getResources().getStringArray(R.array.main_tilte);
        manager = getSupportFragmentManager();
        viewPagerAdapter = new MainViewPagerAdapter(manager, fragments, titles);
        mainViewpager.setOffscreenPageLimit(3);
        mainViewpager.setAdapter(viewPagerAdapter);
        mainTab.setupWithViewPager(mainViewpager);

    }

    private void initCookie() {
        HttpHeaders headers = new HttpHeaders();
        headers.put("Cookie", getCookie());
        OkGo.getInstance().addCommonHeaders(headers);
    }


    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
    }

    private String getCookie() {
        StringBuilder cookie = new StringBuilder();
        cookie.append("DeviceUUID=").append(PhoneUtil.getDeviceUUID(this)).append("; ").append("DeviceType=2; ");
        PackageManager pm = getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(getPackageName(), 0);
            cookie.append("AppVer=").append(pi.versionName).append("; ");
//            appName = pi.applicationInfo.loadLabel(getPackageManager()).toString();
//            appChannel = pm.getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA)
//                    .metaData.get("UMENG_CHANNEL").toString();
//            userAgent = "Android" + ";" + appChannel + ";" + android.os.Build.MODEL + ";" +
//                    android.os.Build.VERSION.SDK_INT
//                    + ";" + android.os.Build.VERSION.RELEASE + ";" + appVer + ";" + deviceUUID;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        //do something

        return cookie.toString();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == REQUEST_CODE) {
            if (resultCode == PermissionsActivity.PERMISSIONS_DENIED)
                AppManager.getAppManager().finishAllActivity();
            else
                initCookie();
        }
    }

    public class MainViewPagerAdapter extends FragmentStatePagerAdapter {
        private List<Fragment> fragments;
        private String[] titles;

        public MainViewPagerAdapter(FragmentManager fm, List<Fragment> fragments, String[] titles) {
            super(fm);
            this.fragments = fragments;
            this.titles = titles;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = titles[position];
            return title;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

}
