package com.github.daweizhou89.reqlist.sample;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.LruCache;

import com.github.daweizhou89.okhttpclientutils.OkHttpClientUtils;
import com.github.daweizhou89.reqlist.ReqList;
import com.github.daweizhou89.reqlist.interfaces.IMemoryCacheManager;

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
        OkHttpClientUtils.config()
                .setUserAgent("daweizhou89/test")   // 设定UserAgent
                .setAssertMainThread(true)          // 打开检测UI线程执行的断言
                .setDebugLog(true);
        ReqList.customize()
                .setDataCacheManager(new LruMemoryCacheManager())
                .init();
    }

    static class LruMemoryCacheManager implements IMemoryCacheManager {

        private LruCache<String, String> mLruCache = new LruCache<>(1 * 1024 * 1024);

        @Override
        public String get(@NonNull String key) {
            return mLruCache.get(key);
        }

        @Override
        public void put(@NonNull String key, @Nullable String data) {
            mLruCache.put(key, data);
        }
    }
}
