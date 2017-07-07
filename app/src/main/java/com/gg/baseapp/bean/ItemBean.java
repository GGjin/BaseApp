package com.gg.baseapp.bean;

import com.gg.baseapp.base.BaseBean;

/**
 * Created by GG on 2017/7/7.
 */

public class ItemBean extends BaseBean {

    private String id;

    public ItemBean(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
