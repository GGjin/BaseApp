package com.gg.baseapp.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.gg.baseapp.R;
import com.gg.baseapp.adapter.MainAdapter;
import com.gg.baseapp.base.BaseFragment;
import com.gg.baseapp.bean.ItemBean;
import com.gg.baseapp.customui.DividerDecoration;
import com.gg.baseapp.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by GG on 2017/7/7.
 */

public class MainFragment extends BaseFragment implements  SwipeRefreshLayout.OnRefreshListener {
    private static final String KEY_TYPE = "TYPE";

    public static final int TYPE_A = 1;
    public static final int TYPE_B = 2;
    public static final int TYPE_C = 3;

    @BindView(R.id.rv_list) RecyclerView mRecyclerView;
    @BindView(R.id.swipeLayout) SwipeRefreshLayout mSwipeRefreshLayout;
    private String mType;
    private List<ItemBean> mData = new ArrayList<>();
    private MainAdapter adapter;

    public static MainFragment startFragment(String type) {
        MainFragment fragment = new MainFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    public void initView() {
        mType = getArguments().getString(KEY_TYPE);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        initAdapter();
        mSwipeRefreshLayout.setRefreshing(true);
        onRefresh();
    }

    private void getData() {
        for (int i = 0; i < 20; i++) {
            mData.add(new ItemBean(mType));
        }
        mSwipeRefreshLayout.setRefreshing(false);
        adapter.setEnableLoadMore(true);
    }


    private void initAdapter() {

        DividerDecoration dividerDecoration = new DividerDecoration(getActivity());
        dividerDecoration.setDividerHeightDip(5);
        dividerDecoration.setDividerColor(ContextCompat.getColor(context, R.color.ging_gray));
        mRecyclerView.addItemDecoration(dividerDecoration);


        adapter = new MainAdapter(mData);
        mRecyclerView.setAdapter(adapter);

        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(final BaseQuickAdapter adapter, final View view, final int position) {
                ToastUtil.showShort(mData.get(position).getId());
            }
        });
        getData();

    }

    @Override
    public void onRefresh() {
        adapter.setEnableLoadMore(false);
        mData.clear();
        getData();
    }

}
