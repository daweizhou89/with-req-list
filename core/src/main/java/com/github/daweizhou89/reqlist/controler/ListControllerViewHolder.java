package com.github.daweizhou89.reqlist.controler;

import android.support.annotation.Nullable;

import com.github.daweizhou89.listview.ISwipeRefreshWrapper;
import com.github.daweizhou89.listview.RecyclerListView;
import com.github.daweizhou89.reqlist.DebugLog;
import com.github.daweizhou89.reqlist.ReqListContext;
import com.github.daweizhou89.reqlist.adapter.BaseListAdapter;
import com.github.daweizhou89.reqlist.adapter.ILoadFooterCreator;
import com.github.daweizhou89.reqlist.view.ILoadView;

import java.util.List;

/**
 * Created by daweizhou89 on 2017/1/24.
 */
public class ListControllerViewHolder {

    /**  */
    protected ReqListContext mReqListContext;
    /**  */
    protected BaseListController mListController;
    /**  */
    protected ILoadView mLoadTipsView;
    /**  */
    protected ISwipeRefreshWrapper mSwipeRefreshWrapper;
    /**  */
    protected RecyclerListView mRecyclerListView;

    public ListControllerViewHolder(BaseListController listController) {
        this.mListController = listController;
        this.mReqListContext = listController.getContextHolder();
    }

    /**
     * 支持动态设置啦
     *
     * @param loadTipsView 加载提示View
     */
    public void setLoadTipsView(ILoadView loadTipsView) {
        mLoadTipsView = loadTipsView;
        if (mLoadTipsView != null) {
            mLoadTipsView.setOnReloadListener(new ILoadView.OnReloadListener() {

                @Override
                public void onReload() {
                    mListController.requestData();
                }
            });
        }
    }

    public final void setSwipeRefreshWrapper(ISwipeRefreshWrapper swipeRefreshWrapper) {
        mSwipeRefreshWrapper = swipeRefreshWrapper;
        // 配置上拉刷新
        mSwipeRefreshWrapper.setOnListRefreshListener(new ISwipeRefreshWrapper.OnListRefreshListener() {

            @Override
            public void onBeforeRefresh() {
                mListController.onBeforeRefresh();
            }

            @Override
            public void onRefresh() {
                mListController.requestData();
            }

            @Override
            public void onLoadMore() {
                mListController.onRequestData(true);
            }

        });
    }

    public final void setRecyclerListView(RecyclerListView recyclerListView) {
        mRecyclerListView = recyclerListView;
        BaseListAdapter listAdapter = mListController.getListAdapter();
        if (listAdapter instanceof ILoadFooterCreator) {
            recyclerListView.setLoadMoreMode(RecyclerListView.LOAD_MORE_MODE_NORMAL);
        }
        mSwipeRefreshWrapper.setAdapter(listAdapter);
    }

    public final ILoadView getLoadTipsView() {
        return mLoadTipsView;
    }

    public final ISwipeRefreshWrapper getSwipeRefreshWrapper() {
        return mSwipeRefreshWrapper;
    }

    public final RecyclerListView getRecyclerView() {
        return mRecyclerListView;
    }

    protected final void onRefreshComplete() {
        mSwipeRefreshWrapper.onRefreshComplete();
    }

    protected final void setLoadState(int state) {
        if (mLoadTipsView != null) {
            mLoadTipsView.setState(state);
        }
    }

    public final void scrollToTop() {
        if (mRecyclerListView != null && !mReqListContext.isEmpty()) {
            mRecyclerListView.scrollToPosition(0);
        }
    }

    public final void smoothScrollToTop() {
        if (mRecyclerListView != null && !mReqListContext.isEmpty()) {
            mRecyclerListView.smoothScrollToPosition(0);
        }
    }

    public final void trySetLoadMoreEnable(@Nullable List<?> data, int pageCount) {
        if (mSwipeRefreshWrapper == null) {
            return;
        }
        int dataSize = data == null ? 0 : data.size();
        trySetLoadMoreEnable(dataSize, pageCount);
    }

    public final void trySetLoadMoreEnable(int dataSize, int pageCount) {
        if (mSwipeRefreshWrapper == null) {
            return;
        }
        if (DebugLog.DEBUG) {
            DebugLog.v(getClass(), "trySetLoadMoreEnable", "dataSize:" + dataSize + "pageCount:" + pageCount);
        }
        if (dataSize < pageCount) {
            mSwipeRefreshWrapper.setLoadMoreEnable(false);
        } else {
            mSwipeRefreshWrapper.setLoadMoreEnable(true);
        }
    }

    public final void onLoadMoreComplete() {
        mSwipeRefreshWrapper.onLoadMoreComplete();
    }

}
