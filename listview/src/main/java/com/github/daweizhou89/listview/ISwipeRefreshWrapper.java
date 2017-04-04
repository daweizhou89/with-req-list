package com.github.daweizhou89.listview;

import android.support.v7.widget.RecyclerView;

/**
 * Created by zhoudawei on 2017/4/4.
 */

public interface ISwipeRefreshWrapper {

    boolean isRefreshing();

    void setRefreshing(boolean refreshing);

    void setLoadMoreEnable(boolean enable);

    void onLoadMoreComplete();

    void onRefreshComplete();

    void setAdapter(RecyclerView.Adapter adapter);

    RecyclerListView getRecyclerView();

    void setOnListRefreshListener(OnListRefreshListener onListRefreshListener);

    /**
     * 刷新／加载更多监听器
     * onLoadMore 在调用 setLoadMoreEnable(true)才有效
     */
    interface OnListRefreshListener extends RecyclerListView.OnLoadMoreListener {
        void onBeforeRefresh();
        void onRefresh();
    }
}
