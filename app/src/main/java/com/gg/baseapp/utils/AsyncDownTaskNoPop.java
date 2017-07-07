package com.gg.baseapp.utils;

import android.os.AsyncTask;

/**
 * Created by GG on 2017/5/31.
 */

public class AsyncDownTaskNoPop extends AsyncTask<Integer, Integer, String> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Integer... params) {
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }

}