package com.gg.baseapp.utils.constants;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by GG on 2017/6/1.
 */

public class LocalBroadcastHelper {

    public static void registerReceiverForActions(LocalBroadcastManager manager, BroadcastReceiver receiver, String[] actions) {
        IntentFilter intentFilter = new IntentFilter();
        for (String action : actions) {
            intentFilter.addAction(action);
        }
        manager.registerReceiver(receiver, intentFilter);
    }
    public static void notifyWXPayResult(LocalBroadcastManager manager, boolean result) {
        Intent intent = new Intent(LocalBroadcastConstants.INTENT_WXPAY_RESULT);
        intent.putExtra(LocalBroadcastConstants.EXTRA_RESULT, result);
        manager.sendBroadcast(intent);
    }

}
