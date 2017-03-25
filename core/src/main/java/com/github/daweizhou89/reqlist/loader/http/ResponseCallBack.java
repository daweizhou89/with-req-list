package com.github.daweizhou89.reqlist.loader.http;

import com.github.daweizhou89.okhttpclientutils.TextResponseCallback;
import com.github.daweizhou89.reqlist.DebugLog;
import com.github.daweizhou89.reqlist.ReqList;

/**
 * Created by zhoudawei on 2017/3/25.
 */
public class ResponseCallBack implements TextResponseCallback {

    protected BaseHttpLoader mLoader;

    public ResponseCallBack(BaseHttpLoader loader) {
        this.mLoader = loader;
    }

    @Override
    public void onSuccess(String url, String response) {
        String cacheKey = mLoader.getCacheKey();
        if (cacheKey != null) {
            ReqList.getDataCacheManager().put(mLoader.getCacheKey(), response);
        }
        mLoader.onResponse(url, response, false);
        mLoader.afterLoading();
    }

    @Override
    public void onFailure(String url, Throwable throwable) {
        if (DebugLog.DEBUG) {
            DebugLog.e(getClass(), "onFailure", url, throwable);
        }
        mLoader.onResponseError(url, throwable, false);
        mLoader.afterLoading();
    }
}
