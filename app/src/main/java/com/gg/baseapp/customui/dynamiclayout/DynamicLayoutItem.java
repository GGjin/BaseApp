package com.gg.baseapp.customui.dynamiclayout;

import android.content.Context;
import android.view.View;

public abstract class DynamicLayoutItem {
	
	protected Context mContext;
	protected View mBodyView;
    protected int mColumnCount;

	public DynamicLayoutItem(Context context, int columnCount) {
		mContext = context;
        mColumnCount = columnCount;
	}
	
	public void bindView(View view){
		mBodyView = view;
		setViewData(mBodyView);
	}
	
	protected abstract void setViewData(View view);
	
	public View getBodyView(){
		return mBodyView;
	}
}
