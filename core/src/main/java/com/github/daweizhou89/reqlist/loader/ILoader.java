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

    void onResponse(R response, boolean more);

    void onResponseError(Throwable throwable, boolean more);

}
