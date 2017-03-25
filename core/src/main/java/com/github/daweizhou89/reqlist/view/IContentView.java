package com.github.daweizhou89.reqlist.view;

/**
 * Created by daweizhou89 on 2017/1/13.
 */
public interface IContentView {
    void requestData();
    void refreshData();
    boolean needLoad();
}
