package com.gg.baseapp.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.gg.baseapp.utils.AppManager;

import butterknife.ButterKnife;

/**
 *
 */

public abstract class BaseActivity extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        init();
    }

    public abstract int getLayoutId();


    private final void init() {
        initView();
        initData();
    }

    public abstract void initView();

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AppManager.getAppManager().finishActivity();
    }

    public void initData() {
    }

}
