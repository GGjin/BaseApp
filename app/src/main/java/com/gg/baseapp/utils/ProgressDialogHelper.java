package com.gg.baseapp.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;

import com.gg.baseapp.R;


/**
 * Created by GG on 2017/5/17.
 */

public class ProgressDialogHelper {

    private  Context mContext;
    private static ProgressDialogHelper progressDialogHelper = new ProgressDialogHelper();
    private ProgressDialog mProgressDialog;

    private ProgressDialogHelper() {
    }

    public static ProgressDialogHelper getInstance() {
        return progressDialogHelper;
    }


    public void initProgressDialog(Context context) {
        mContext = context;
        mProgressDialog = new ProgressDialog(mContext);//加参数,ProgressDialog.THEME_HOLO_LIGHT可设置为白色风格
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMessage(mContext.getResources().getString(R.string.loading));
    }

    public void showProgressDialog() {
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.setMessage(mContext.getResources().getString(R.string.loading));
            mProgressDialog.show();
        }
    }

    protected void showProgressDialog(String title, String message) {
        if (!TextUtils.isEmpty(title)) mProgressDialog.setTitle(title);
        if (!TextUtils.isEmpty(message)) mProgressDialog.setMessage(message);
        if (!mProgressDialog.isShowing())
            mProgressDialog.show();
    }

    public void showSubmitProgressDialog() {
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.setMessage(mContext.getResources().getString(R.string.submitting));
            mProgressDialog.show();
        }
    }

    public void hideProgressDialog() {
        if (mProgressDialog.isShowing())
            mProgressDialog.cancel();
    }

    public void sayNetworkError() {
        ToastUtil.showLong(mContext, R.string.network_error);
    }

    protected void saySuccess() {
        ToastUtil.showLong(mContext, R.string.operation_success);
    }
}
