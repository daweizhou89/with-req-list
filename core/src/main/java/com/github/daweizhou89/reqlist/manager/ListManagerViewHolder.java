package com.github.daweizhou89.reqlist.manager;

import android.support.annotation.Nullable;

import com.github.daweizhou89.listview.RecyclerListView;
import com.github.daweizhou89.listview.SwipeRefreshListLayout;
import com.github.daweizhou89.reqlist.DebugLog;
import com.github.daweizhou89.reqlist.adapter.AbstractListAdapter;
import com.github.daweizhou89.reqlist.view.ILoadTips;

import java.util.List;

/**
 * Created by daweizhou89 on 2017/1/24.
 */
public class ListManagerViewHolder {

    /**  */
    protected AbstractListManager mListManager;

    /**  */
    protected ILoadTips mLoadTipsView;

    /**  */
    protected SwipeRefreshListLayout mSwipeRefreshListLayout;

    public ListManagerViewHolder(AbstractListManager listManager) {
        this.mListManager = listManager;
    }

    protected void onAfterSetLoadTipsView(ILoadTips loadTipsView) {
        // TODO nothing
    }

    protected void onAfterSetSwipeRefreshListLayout(SwipeRefreshListLayout swipeRefreshListLayout) {
        // TODO nothing
    }

    /**
     * 支持动态设置啦
     *
     * @param loadTipsView 加载提示View
     */
    public void setLoadTipsView(ILoadTips loadTipsView) {
        mLoadTipsView = loadTipsView;
        if (mLoadTipsView != null) {
            mLoadTipsView.setOnReloadListener(new ILoadTips.OnReloadListener() {

                @Override
                public void onReload() {
                    mListManager.requestData();
                }
            });
        }
        onAfterSetLoadTipsView(loadTipsView);
    }

    /**
     * 支持动态设置啦
     *
     * @param swipeRefreshListView 列表
     */
    public final void setSwipeRefreshListView(SwipeRefreshListLayout swipeRefreshListLayout) {
        mSwipeRefreshListLayout = swipeRefreshListLayout;
        // 配置上拉刷新
        mSwipeRefreshListLayout.setOnListRefreshListener(new SwipeRefreshListLayout.OnListRefreshListener() {

            @Override
            public void onBeforeRefresh() {
                mListManager.onBeforeRefresh();
            }

            @Override
            public void onRefresh() {
                mListManager.requestData();
            }

            @Override
            public void onLoadMore() {
                mListManager.onRequestMoreAppend();
            }

        });
        AbstractListAdapter listAdapter = mListManager.getListAdapter();
        if (listAdapter instanceof AbstractListAdapter.INormalLoadMore) {
            mSwipeRefreshListLayout.getRecyclerView().setLoadMoreMode(RecyclerListView.LOAD_MORE_MODE_NORMAL);
        }
        mSwipeRefreshListLayout.setAdapter(listAdapter);
        onAfterSetSwipeRefreshListLayout(swipeRefreshListLayout);
    }

    public final ILoadTips getLoadTipsView() {
        return mLoadTipsView;
    }

    public final SwipeRefreshListLayout getSwipeRefreshListLayout() {
        return mSwipeRefreshListLayout;
    }

    public final RecyclerListView getRecyclerView() {
        return mSwipeRefreshListLayout.getRecyclerView();
    }

    protected final void onRefreshComplete() {
        mSwipeRefreshListLayout.onRefreshComplete();
    }

    protected final void setEmptyTipsText(String emptyTipsText) {
        if (mLoadTipsView != null) {
            mLoadTipsView.setEmptyText(emptyTipsText);
        }
    }

    protected final void setLoadTips(int tips) {
        if (mLoadTipsView != null) {
            mLoadTipsView.setTips(tips);
        }
    }

    public final void scrollToTop() {
        if (mSwipeRefreshListLayout != null && !mListManager.isEmpty()) {
            mSwipeRefreshListLayout.getRecyclerView().scrollToPosition(0);
        }
    }

    public final void smoothScrollToTop() {
        if (mSwipeRefreshListLayout != null && !mListManager.isEmpty()) {
            mSwipeRefreshListLayout.getRecyclerView().smoothScrollToPosition(0);
        }
    }

    /***
     * 需要上拉加载更多的话，掉这个方法
     *
     * @param datas     请求返回数据列表
     * @param pageCount 请求的页大小
     */
    public final void trySetLoadMoreEnable(@Nullable List<?> datas, int pageCount) {
        if (mSwipeRefreshListLayout == null) {
            return;
        }
        int dataSize = datas == null ? 0 : datas.size();
        trySetLoadMoreEnable(dataSize, pageCount);
    }

    public final void trySetLoadMoreEnable(int dataSize, int pageCount) {
        if (mSwipeRefreshListLayout == null) {
            return;
        }
        if (DebugLog.DEBUG) {
            DebugLog.v(getClass(), "trySetLoadMoreEnable", "dataSize:" + dataSize + "pageCount:" + pageCount);
        }
        if (dataSize < pageCount) {
            mSwipeRefreshListLayout.setLoadMoreEnable(false);
        } else {
            mSwipeRefreshListLayout.setLoadMoreEnable(true);
        }
    }

    public final void onLoadMoreComplete() {
        mSwipeRefreshListLayout.onLoadMoreComplete();
    }

}
