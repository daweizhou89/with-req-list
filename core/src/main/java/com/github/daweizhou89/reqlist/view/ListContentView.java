package com.github.daweizhou89.reqlist.view;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.util.AttributeSet;
import android.view.View;

import com.github.daweizhou89.listview.RecyclerListView;
import com.github.daweizhou89.listview.SwipeRefreshListLayout;
import com.github.daweizhou89.reqlist.manager.AbstractListManager;

/**
 * Created by daweizhou89 on 2017/1/13.
 */
public class ListContentView extends AbstractContentView {

    /** 列表Manager */
    protected AbstractListManager mListManager;
    /** 可以刷新的列表 */
    protected SwipeRefreshListLayout mSwipeRefreshListLayout;
    /** 是否需要重新标记 */
    private boolean mNeedRequesting;

    public ListContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SwipeRefreshListLayout getSwipeRefreshListLayout() {
        return mSwipeRefreshListLayout;
    }

    public RecyclerListView getRecyclerView() {
        return mSwipeRefreshListLayout != null ? mSwipeRefreshListLayout.getRecyclerView() : null;
    }

    @Override
    @CallSuper
    public void requestData() {
        mNeedRequesting = false;
        mListManager.requestData();
    }

    @Override
    @CallSuper
    public void refreshData() {
        mNeedRequesting = false;
        mListManager.getListManagerViewHolder().smoothScrollToTop();
        if (!mListManager.isEmpty()) {
            if (!mSwipeRefreshListLayout.isRefreshing()) {
                mSwipeRefreshListLayout.setRefreshing(true);
            }
            mListManager.requestData();
        } else {
            mListManager.requestData();
        }
    }

    @CallSuper
    public void smoothScrollToTop() {
        mListManager.getListManagerViewHolder().smoothScrollToTop();
    }

    @CallSuper
    public void setNeedRequesting(boolean needRequesting) {
        mNeedRequesting = needRequesting;
    }

    @Override
    @CallSuper
    public boolean needRequesting() {
        if (mListManager == null) {
            return mNeedRequesting;
        }
        return mNeedRequesting || mListManager.needRequesting();
    }

    public AbstractListManager getListManager() {
        return mListManager;
    }

    public InitHelper getInitHelper() {
        return new InitHelper(this);
    }

    public static class InitHelper {

        final ListContentView listContentView;

        int loadTipsId;

        int swipeRefreshListViewId;

        AbstractListManager listManager;

        boolean requestData = true;

        private InitHelper(ListContentView listContentView) {
            this.listContentView = listContentView;
        }

        public InitHelper setLoadTipsId(int loadTipsId) {
            this.loadTipsId = loadTipsId;
            return this;
        }

        public InitHelper setSwipeRefreshListViewId(int swipeRefreshListViewId) {
            this.swipeRefreshListViewId = swipeRefreshListViewId;
            return this;
        }

        public InitHelper setListManager(AbstractListManager listManager) {
            this.listManager = listManager;
            return this;
        }

        public InitHelper setRequestData(boolean requestData) {
            this.requestData = requestData;
            return this;
        }

        public void init() {
            ILoadTips loadTips = null;
            SwipeRefreshListLayout swipeRefreshListView = null;
            if (loadTipsId > 0) {
                loadTips = (ILoadTips) listContentView.findViewById(loadTipsId);
            }
            if (swipeRefreshListViewId > 0) {
                swipeRefreshListView = (SwipeRefreshListLayout) listContentView.findViewById(swipeRefreshListViewId);
            }
            for (int i = 0; i < listContentView.getChildCount(); i++) {
                if (swipeRefreshListView != null && loadTips != null) {
                    break;
                }
                View child = listContentView.getChildAt(i);
                if (child instanceof SwipeRefreshListLayout) {
                    swipeRefreshListView = (SwipeRefreshListLayout) child;
                } else if (child instanceof ILoadTips) {
                    loadTips = (ILoadTips) child;
                }
            }

            listContentView.mSwipeRefreshListLayout = swipeRefreshListView;
            listManager.getListManagerViewHolder().setLoadTipsView(loadTips);
            listManager.getListManagerViewHolder().setSwipeRefreshListView(swipeRefreshListView);
            listContentView.mListManager = listManager;
            if (requestData) {
                listContentView.refreshData();
            }
        }
    }

}
