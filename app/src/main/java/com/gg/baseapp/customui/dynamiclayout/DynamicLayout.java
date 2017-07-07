package com.gg.baseapp.customui.dynamiclayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.gg.baseapp.utils.DensityUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class DynamicLayout {

	private static final String PREF_HOME_PAGE = "dynamic_layout";
	private static final String SEPARATOR = ",";
	protected Context mContext;
	protected LinearLayout mContainer;
    protected int mColumnCount;
	protected List<DynamicLayoutItem> mItemList;
	protected Map<String, DynamicLayoutItem> mItemMap;
	protected List<String> mItemKeyList;
	protected SharedPreferences mPreferences;
	private List<View> rowViews = new ArrayList<>();
	protected Handler mHandler;

	public DynamicLayout(Context context,LinearLayout container, int columnCount) {
		mContext = context;
		mContainer = container;
		mContainer.setVisibility(View.VISIBLE);
		mPreferences = mContext.getSharedPreferences(PREF_HOME_PAGE, 0);
		mContainer.removeAllViews();
		mContainer.setOrientation(LinearLayout.VERTICAL);
        mColumnCount = columnCount;//必须在下面两个函数（fillInItemViews/refreshRowState）之前执行,否则获得列数为0，会出现除零错误
		fillInItemViews(getPrebuildItemCount());
		refreshRowState(getPrebuildItemCount(), false);
	}
	
	public void startLoadData(){
		buildBlockItemMap();
		buildItemKeyList();
		pickUpItems();
		fillInItemViews(getItemCount());
		refreshRowState(getItemCount(), true);
		doAfterCreated();
	}
	
	protected abstract void doAfterCreated();
	
	private void buildItemKeyList(){
		if (mItemKeyList == null) {
			mItemKeyList = new ArrayList<>();
		}

		if (getPreferenceKey() == null){
			mItemKeyList = getDefaultItemKeyList();
		} else {
			Map<String, Object> allPrefs = (Map<String, Object>) mPreferences.getAll();
			if (allPrefs == null || !allPrefs.containsKey(getPreferenceKey())) {//not edited
				mItemKeyList = getDefaultItemKeyList();
			} else {
				mItemKeyList = getItemKeyListFromPref();
			}
		}
	}
	
	private List<String> getItemKeyListFromPref(){
		String str = mPreferences.getString(getPreferenceKey(), "");
		List<String> result = new ArrayList<>();
		if (str!= null && !str.equals("")) {
			String[] keySet = str.split(SEPARATOR);
			Collections.addAll(result, keySet);
		}
		return result;
	}
	
	@SuppressLint("CommitPrefEdits")
	protected void saveItemKeyList(){
		SharedPreferences.Editor editor = mPreferences.edit();
		if (mItemKeyList.size() == 0) {
			editor.remove(getPreferenceKey());
		} else {
			StringBuilder builder = new StringBuilder();
			for(int i = 0; i< mItemKeyList.size(); i++){
				if (i > 0) {
					builder.append(SEPARATOR);
				}
				builder.append(mItemKeyList.get(i));
			}
			editor.putString(getPreferenceKey(), builder.toString());
		}
		editor.commit();
	}
	
	private void pickUpItems(){
		if (mItemList == null) {
			mItemList = new ArrayList<>();
		} else {
			mItemList.clear();
		}
		
		for(String itemKey : mItemKeyList){
			DynamicLayoutItem item = mItemMap.get(itemKey);
			if (item == null) {
				//throw new IllegalStateException("Could not find block item : "+itemKey);
				continue; //to avoid crash
			}
			mItemList.add(item);
		}
	}
	
	private void fillInItemViews(int itemCount){
		if (itemCount == 0) {
			mContainer.setVisibility(View.GONE);
		} else {
			mContainer.setVisibility(View.VISIBLE);
		}
		int rowCount = (itemCount - 1)/ getColumnCount() + 1;
		for(int i = 0; i<rowCount; i++){
			LinearLayout currentRow;
			if (rowViews.size() > i *2) {
				// row already exists
				currentRow = (LinearLayout) rowViews.get(i*2);
			} else { 
				// need to add a new row
				if (i > 0) { 
					// need to add a horizontal divider
					ViewGroup.LayoutParams dividerParams = new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.FILL_PARENT, DensityUtils.dp2px(mContext, 1));
					View divider = getHorizontalDivider();
					rowViews.add(divider);
					mContainer.addView(divider, dividerParams);
				}
				// create a new row and add it into container
				currentRow = createNewRow();
				rowViews.add(currentRow);
				ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
						ViewGroup.LayoutParams.FILL_PARENT, 
						ViewGroup.LayoutParams.WRAP_CONTENT);
				mContainer.addView(currentRow, params);
			}
		}
	}
	
	protected LinearLayout createNewRow(){
		LinearLayout row = new LinearLayout(mContext);
		row.setGravity(Gravity.LEFT);
		row.setOrientation(LinearLayout.HORIZONTAL);
		
		for(int i=0; i<getColumnCount(); i++){
			if (i > 0) {// add a divider for right item
				ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
						DensityUtils.dp2px(mContext, 1), ViewGroup.LayoutParams.FILL_PARENT);
				row.addView(getVertialDivider(), params);
			}
			// add item
			LinearLayout.LayoutParams itemParams = 
					new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
							LinearLayout.LayoutParams.WRAP_CONTENT);
			if (getColumnCount() > 1) {
				itemParams = new LinearLayout.LayoutParams(
						0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
			}
			View itemView = LayoutInflater.from(mContext).
					inflate(getItemLayoutId(), null);
			itemView.setId(i % getColumnCount());
			if (itemParams == null) {
				row.addView(itemView);
			} else {
				row.addView(itemView, itemParams);
			}
			
		}
		return row;
	}
	
	private View getVertialDivider(){
		View dividerView = new View(mContext);
		dividerView.setBackgroundColor(Color.TRANSPARENT);
		return dividerView;
	}
	
    protected View getHorizontalDivider() {
		View dividerView = new View(mContext);
		dividerView.setBackgroundColor(Color.TRANSPARENT);
		return dividerView;
	}
	
	protected int getItemCount(){
		return mItemKeyList.size();
	}
	
	private void refreshRowState(int count, boolean needBindData){
		int rowCount = (count - 1)/ getColumnCount() + 1;
		for(int i=0; i<rowViews.size(); i++){
			View view = rowViews.get(i);
			if (i <= (rowCount -1) * 2) { 
				//rows and dividers that need to show
				view.setVisibility(View.VISIBLE);
				if (i % 2 == 0) {
					// it's a row
					if (needBindData) {
						bindRowWithItems(i / 2, view);
					}
				}
			} else {
				//rows and dividers that need to hide
				view.setVisibility(View.GONE);
			}
		}
	}
	
	private void bindRowWithItems(int rowIndex, View row){
		LinearLayout currentRow = (LinearLayout) row;
		int start = rowIndex * getColumnCount();
		int end = Math.min((rowIndex + 1) * getColumnCount(), getItemCount());
		List<DynamicLayoutItem> subItemList = mItemList.subList(start, end);
		for(int i = 0; i<currentRow.getChildCount(); i++){
			View child = currentRow.getChildAt(i);
			if (i < subItemList.size() * 2) {
				// items and dividers that need to show
				child.setVisibility(View.VISIBLE);
				if (i % 2 == 0) {
					// it's an item view
					int index = i/2;
					DynamicLayoutItem item = subItemList.get(index);
					item.bindView(child);
				}
			} else {
				// items and dividers that need to hide
				child.setVisibility(View.INVISIBLE);
			}
		}
	}
	
	protected String getPreferenceKey(){
		return null;
	}
	
	protected abstract List<String> getDefaultItemKeyList();
	
	protected abstract void buildBlockItemMap();
	
	protected abstract int getItemLayoutId();
	
	protected abstract int getColumnCount();
	
	protected abstract int getPrebuildItemCount();
	
	public void refresh(){
		buildItemKeyList();
		pickUpItems();
		fillInItemViews(getItemCount());
		refreshRowState(getItemCount(), true);
	}
	
	public View getView(){
		return mContainer;
	}
	
	public void onResume(){}
}
