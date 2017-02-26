package com.github.daweizhou89.reqlist.view;

/**
 * Created by daweizhou89 on 2017/1/13.
 */
public interface ILoadTips {

    int TIPS_NULL = -1;
    /***
     * 加载中
     */
    int TIPS_LOADING = 0;
    /***
     * 加载失败
     */
    int TIPS_LOADED_FAIL = 1;
    /***
     * 加载为空
     */
    int TIPS_LOADED_EMPTY = 2;
    /***
     * 加载成功
     */
    int TIPS_LOADED_OK = 3;
    /***
     * 没有网络
     */
    int TIPS_LOADED_NO_NETWORK = 4;

    void setTips(int tips);

    void setEmptyText(String emptyText);

    void setOnReloadListener(OnReloadListener onReloadListener);

    interface OnReloadListener {
        void onReload();
    }

}
