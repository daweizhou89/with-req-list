package com.github.daweizhou89.okhttpclientutils;

import android.util.Log;

/**
 * Created by daweizhou89 on 16/8/4.
 */
class DebugLog {

    public static boolean DEBUG = Config.sDebugLog;
    public static final String DEBUG_TAG = "with-req-list";

    public static void d(Class<?> clazz, String method, String msg) {
        if (DEBUG) {
            d(wrapMessage(clazz, method, msg));
        }
    }

    public static void d(String msg) {
        if (DEBUG) {
            d(DEBUG_TAG, msg);
        }
    }

    public static void e(String msg, Throwable e) {
        if (DEBUG) {
            e(DEBUG_TAG, msg, e);
        }
    }

    public static void e(Throwable e) {
        if (DEBUG) {
            e("", e);
        }
    }

    public static void e(Class<?> clazz, String method, Throwable e) {
        if (DEBUG) {
            e(clazz, method, "", e);
        }
    }

    public static void e(Class<?> clazz, String method, String msg, Throwable e) {
        if (DEBUG) {
            e(wrapMessage(clazz, method, msg), e);
        }
    }

    private static String wrapMessage(Class<?> clazz, String method, String msg) {
        StringBuilder sb = new StringBuilder()
                .append("{")
                .append(clazz.getName())
                .append("}.")
                .append("{")
                .append(method)
                .append("}");
        if (msg != null) {
            sb.append("-");
            sb.append(msg);
        }
        return sb.toString();
    }

    public static void e(String tag, String msg, Throwable e) {
        if (DEBUG) {
            Log.e(tag, msg == null ? "" : msg, e);
        }
    }

    public static void d(String tag, String msg) {
        if (DEBUG) {
            Log.d(tag, msg == null ? "" : msg);
        }
    }

}
