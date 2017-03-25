package com.github.daweizhou89.reqlist.loader.http;

import com.github.daweizhou89.reqlist.DebugLog;
import com.github.daweizhou89.reqlist.ReqList;
import com.github.daweizhou89.reqlist.controler.BaseListController;

/**
 * Created by zhoudawei on 2017/3/25.
 */

public class HttpCacheHelper {

    private BaseHttpLoader mLoader;

    private BaseListController mListController;

    public HttpCacheHelper(BaseHttpLoader loader, BaseListController listController) {
        this.mLoader = loader;
        this.mListController = listController;
    }

    protected void initCacheData() {
        final String cacheKey = mLoader.getCacheKey();
        if (cacheKey != null) {
            try {
                String cacheData = ReqList.getDataCacheManager().get(cacheKey);
                if (cacheData != null) {
                    if (DebugLog.DEBUG) {
                        DebugLog.d(getClass(), "initCacheDataInternal", cacheKey + "," + cacheData);
                    }
                    mLoader.onResponse(cacheKey, cacheData, false);
                }
                if (!mLoader.isEmpty()) {
                    mListController.sendUpdateMessage();
                }
            } catch (Exception e) {
                DebugLog.e(getClass(), "initCacheData", e);
            }
        }
    }
}
