package com.github.daweizhou89.reqlist.loader;

/**
 * Created by daweizhou89 on 16/6/29.
 */
public interface ILoader<R> {

    /** always load(false) */
    void load();

    void load(boolean more, Object... inputs);

    void build(Object... inputs);

    boolean isEmpty();

    boolean isLoading();

    int getLoadType();

    /** key：url or data key */
    void onResponse(String key, R response, boolean more);

    /** key：url or data key */
    void onResponseError(String key, Throwable throwable, boolean more);

}
