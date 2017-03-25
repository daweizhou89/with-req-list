package com.github.daweizhou89.reqlist.adapter;

/**
 * Created by zhoudawei on 2017/3/24.
 */
public interface ILoadFooterCreator {

    enum State {
        LOADING_MORE,
        NO_MORE
    }

    void onLoadFooterStateChanged(State state);
}
