package com.github.daweizhou89.okhttpclientutils;

import android.net.Uri;

import java.io.Serializable;
import java.util.HashMap;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * Created by daweizhou89 on 16/8/11.
 */
public class RequestParams implements Serializable {

    protected final HashMap<String, String> urlParams;

    public RequestParams() {
        urlParams = new HashMap<>();
    }

    public void put(String key, String value) {
        if(key != null && value != null) {
            this.urlParams.put(key, value);
        }
    }

    public void put(String key, Object value) {
        if(key != null && value != null) {
            this.urlParams.put(key, value.toString());
        }
    }

    public void put(String key, int value) {
        if(key != null) {
            this.urlParams.put(key, String.valueOf(value));
        }
    }

    public void put(String key, long value) {
        if(key != null) {
            this.urlParams.put(key, String.valueOf(value));
        }
    }

    public void remove(String key) {
        this.urlParams.remove(key);
    }

    @Override
    public String toString() {
        Uri.Builder builder = new Uri.Builder();
        for (String key : this.urlParams.keySet()) {
            builder.appendQueryParameter(key, urlParams.get(key));
        }
        return builder.build().getEncodedQuery();
    }

    public RequestBody getRequestBody() {
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : this.urlParams.keySet()) {
            builder.add(key, urlParams.get(key));
        }
        return builder.build();
    }
}
