package com.github.daweizhou89.reqlist.view;

/**
 * Created by daweizhou89 on 2017/1/13.
 */
public interface ILoadView {

    int STATE_NULL = 0;
    /***
     * 加载中
     */
    int STATE_LOADING = 1;
    /***
     * 加载失败
     */
    int STATE_LOADED_FAIL = 2;
    /***
     * 加载为空
     */
    int STATE_LOADED_EMPTY = 3;
    /***
     * 加载成功
     */
    int STATE_LOADED_OK = 4;
    /***
     * 没有网络
     */
    int STATE_LOADED_NO_NETWORK = 5;

    void setState(int state);

    void setOnReloadListener(OnReloadListener onReloadListener);

    interface OnReloadListener {
        void onReload();
    }

}
