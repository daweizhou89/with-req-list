package com.github.daweizhou89.reqlist;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by daweizhou89 on 2017/2/18.
 */
public interface IDataCacheManager {

    String get(@NonNull String key);

    void put(@NonNull String key, @Nullable String data);

}
