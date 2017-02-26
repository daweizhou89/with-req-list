package com.github.daweizhou89.reqlist.sample;

import android.app.Application;

import com.github.daweizhou89.okhttpclientutils.OkHttpClientUtils;

/**
 * ****  ****  *  ****
 * *  *  *  *     *
 * ****  *  *  *     *
 * *     *  *  *     *
 * ****  ****  *     *
 * <p>
 * Created by daweizhou89 on 2017/2/26.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        OkHttpClientUtils.init(null);
    }
}
