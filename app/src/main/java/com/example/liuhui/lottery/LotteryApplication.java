package com.example.liuhui.lottery;

import android.app.Application;

/**
 * TODO: description
 * Date: 2017-05-24
 *
 * @author liuhui8
 */
public class LotteryApplication extends Application {
    private static LotteryApplication mInstance = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static LotteryApplication getAppContext() {
        return mInstance;
    }
}
