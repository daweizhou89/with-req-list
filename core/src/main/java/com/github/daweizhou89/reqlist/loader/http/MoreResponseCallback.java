package com.github.daweizhou89.reqlist.loader.http;

import com.github.daweizhou89.reqlist.DebugLog;

/**
 * Created by zhoudawei on 2017/3/25.
 */
public class MoreResponseCallback extends ResponseCallBack {

    public MoreResponseCallback(BaseHttpLoader loader) {
        super(loader);
    }

    @Override
    public void onSuccess(String url, String response) {
        if (mLoader.isEffectiveTime()) {
            mLoader.updatePageNo();
            mLoader.onResponse(url, response, true);
            mLoader.sendUpdateMessage();
        }
        mLoader.onLoadMoreComplete();
    }

    @Override
    public void onFailure(String url, Throwable throwable) {
        if (DebugLog.DEBUG) {
            DebugLog.e(getClass(), "onFailure", url, throwable);
        }
        mLoader.onResponseError(url, throwable, true);
        mLoader.onLoadMoreComplete();
    }
}
