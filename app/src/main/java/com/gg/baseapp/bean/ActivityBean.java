package com.gg.baseapp.bean;


import android.os.Parcel;
import android.os.Parcelable;

import butterknife.Unbinder;

/**
 * Created by GG on 2017/5/15.
 */

public class ActivityBean implements Parcelable {

    private Unbinder unbinder;

    public void setUnbinder(Unbinder unbinder) {
        this.unbinder = unbinder;
    }

    public Unbinder getUnbinder() {
        return unbinder;
    }

    public ActivityBean() {

    }

    public ActivityBean(Parcel in) {
    }

    public static final Creator<ActivityBean> CREATOR = new Creator<ActivityBean>() {
        @Override
        public ActivityBean createFromParcel(Parcel in) {
            return new ActivityBean(in);
        }

        @Override
        public ActivityBean[] newArray(int size) {
            return new ActivityBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
