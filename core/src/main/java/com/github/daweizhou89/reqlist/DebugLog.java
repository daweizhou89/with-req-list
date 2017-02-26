package com.github.daweizhou89.reqlist;

import android.util.Log;


public class DebugLog {

    public static final boolean DEBUG = true;
    public static final String DEBUG_TAG = "with-req-list";

    public static void i(Class<?> clazz, String method, String msg) {
        if (DEBUG) {
            i(wrapMessage(clazz, method, msg));
        }
    }

    public static void i(String msg) {
        if (DEBUG) {
            i(DEBUG_TAG, msg);
        }
    }

    public static void v(Class<?> clazz, String method, String msg) {
        if (DEBUG) {
            v(wrapMessage(clazz, method, msg));
        }
    }

    public static void v(String msg) {
        if (DEBUG) {
            v(DEBUG_TAG, msg);
        }
    }

    public static void w(Class<?> clazz, String method, String msg) {
        if (DEBUG) {
            w(wrapMessage(clazz, method, msg));
        }
    }

    public static void w(String msg) {
        if (DEBUG) {
            w(DEBUG_TAG, msg);
        }
    }

    public static void d(Class<?> clazz, String method, String msg) {
        if (DEBUG) {
            d(wrapMessage(clazz, method, msg));
        }
    }

    public static void d(String msg) {
        if (DEBUG) {
            d(DEBUG_TAG, msg == null ? "" : msg);
        }
    }

    public static void e(String msg) {
        if (DEBUG) {
            e(DEBUG_TAG, msg);
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

    public static void i(String tag, String msg) {
        if (DEBUG) {
            Log.i(tag, msg == null ? "" : msg);
        }
    }

    public static void v(String tag, String msg) {
        if (DEBUG) {
            Log.v(tag, msg == null ? "" : msg);
        }
    }

    public static void w(String tag, String msg) {
        if (DEBUG) {
            Log.w(tag, msg == null ? "" : msg);
        }
    }

    public static void e(String tag, String msg) {
        if (DEBUG) {
            Log.e(tag, msg == null ? "" : msg);
        }
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
