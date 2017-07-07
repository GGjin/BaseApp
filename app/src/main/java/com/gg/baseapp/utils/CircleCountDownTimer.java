package com.gg.baseapp.utils;

import android.os.CountDownTimer;


/**
 */
public class CircleCountDownTimer extends CountDownTimer {
    private static final long MILLIS_IN_FUTURE = 6 * 1000;
    private static final long COUNT_DOWN_INTERVAL = 1 * 1000;

    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    public CircleCountDownTimer(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }


    @Override
    public void onTick(long millisUntilFinished) {
        int progress = (int) (millisUntilFinished / 1000);
    }

    @Override
    public void onFinish() {
    }

}
