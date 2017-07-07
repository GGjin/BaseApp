package com.gg.baseapp.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gg.baseapp.R;
import com.gg.baseapp.bean.ItemBean;

import java.util.List;


/**
 * Created by GG on 2017/5/22.
 */

public class MainAdapter extends BaseQuickAdapter<ItemBean, BaseViewHolder> {


    public MainAdapter(@Nullable List<ItemBean> data) {
        super(R.layout.item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ItemBean item) {
        helper.setText(R.id.text, item.getId());
    }

}
