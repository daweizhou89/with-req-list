package com.github.daweizhou89.okhttpclientutils;

/**
 * Created by daweizhou89 on 16/8/4.
 */
public interface TextResponseCallback {
    void onSuccess(String url, String response);

    void onFailure(String url, Throwable throwable);
}
