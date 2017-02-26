/*
 * 文 件 名:  TaskManager.java
 * 版    权:  3G
 * 描    述:  <描述>
 * 修 改 人:  caoshilei
 * 修改时间:  2014-9-12
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.github.daweizhou89.okhttpclientutils;

import android.os.Looper;

/**
 * Created by daweizhou89 on 16/8/4.
 */
public class Assert {

    public static void assertMainThread() {
        if (Looper.getMainLooper().getThread() != Thread.currentThread()) {
            throw new IllegalStateException("Must be called on the main thread.");
        }
    }

}
