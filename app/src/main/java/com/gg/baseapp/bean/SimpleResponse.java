package com.gg.baseapp.bean;


import com.gg.baseapp.base.BaseBean;

/**
 * Created by GG on 2017/5/19.
 */

public class SimpleResponse<T> extends BaseBean {


    private static final long serialVersionUID = -1477609349345966116L;

    public int Status;
    public String Msg;

    public GingResponse toGingResponse() {
        GingResponse gingResponse = new GingResponse();
        gingResponse.Status = Status;
        gingResponse.Msg = Msg;
        return gingResponse;
    }
}
