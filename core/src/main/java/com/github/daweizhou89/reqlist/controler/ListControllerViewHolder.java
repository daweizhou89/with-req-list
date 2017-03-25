package com.github.daweizhou89.reqlist.controler;

import android.support.annotation.Nullable;

import com.github.daweizhou89.listview.RecyclerListView;
import com.github.daweizhou89.listview.SwipeRefreshList;
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
    protected SwipeRefreshList mSwipeRefreshList;
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

    /**
     * 支持动态设置啦
     *
     * @param swipeRefreshList 列表
     */
    public final void setSwipeRefreshList(SwipeRefreshList swipeRefreshList) {
        mSwipeRefreshList = swipeRefreshList;
        // 配置上拉刷新
        mSwipeRefreshList.setOnListRefreshListener(new SwipeRefreshList.OnListRefreshListener() {

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
        mSwipeRefreshList.setAdapter(listAdapter);
    }

    public final ILoadView getLoadTipsView() {
        return mLoadTipsView;
    }

    public final SwipeRefreshList getSwipeRefreshListLayout() {
        return mSwipeRefreshList;
    }

    public final RecyclerListView getRecyclerView() {
        return mRecyclerListView;
    }

    protected final void onRefreshComplete() {
        mSwipeRefreshList.onRefreshComplete();
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

    /***
     * 需要上拉加载更多的话，掉这个方法
     *
     * @param datas     请求返回数据列表
     * @param pageCount 请求的页大小
     */
    public final void trySetLoadMoreEnable(@Nullable List<?> datas, int pageCount) {
        if (mSwipeRefreshList == null) {
            return;
        }
        int dataSize = datas == null ? 0 : datas.size();
        trySetLoadMoreEnable(dataSize, pageCount);
    }

    public final void trySetLoadMoreEnable(int dataSize, int pageCount) {
        if (mSwipeRefreshList == null) {
            return;
        }
        if (DebugLog.DEBUG) {
            DebugLog.v(getClass(), "trySetLoadMoreEnable", "dataSize:" + dataSize + "pageCount:" + pageCount);
        }
        if (dataSize < pageCount) {
            mSwipeRefreshList.setLoadMoreEnable(false);
        } else {
            mSwipeRefreshList.setLoadMoreEnable(true);
        }
    }

    public final void onLoadMoreComplete() {
        mSwipeRefreshList.onLoadMoreComplete();
    }

}
