package com.github.daweizhou89.reqlist.view;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.github.daweizhou89.listview.ISwipeRefreshWrapper;
import com.github.daweizhou89.reqlist.controler.BaseListController;

/**
 * Created by daweizhou89 on 2017/1/13.
 */
public class ListContentView extends FrameLayout implements IContentView {

    /** 列表Manager */
    protected BaseListController mListController;
    /** 可以刷新的列表 */
    protected ISwipeRefreshWrapper mSwipeRefreshWrapper;

    public ListContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    @CallSuper
    public void requestData() {
        mListController.requestData();
    }

    @Override
    @CallSuper
    public void refreshData() {
        mListController.getListManagerViewHolder().smoothScrollToTop();
        if (!mListController.isEmpty()) {
            if (!mSwipeRefreshWrapper.isRefreshing()) {
                mSwipeRefreshWrapper.setRefreshing(true);
            }
            mListController.requestData();
        } else {
            mListController.requestData();
        }
    }

    @CallSuper
    public void smoothScrollToTop() {
        mListController.getListManagerViewHolder().smoothScrollToTop();
    }

    @Override
    @CallSuper
    public boolean needLoad() {
        return mListController.needLoad();
    }

    public BaseListController getListController() {
        return mListController;
    }

    public InitHelper getInitHelper() {
        return new InitHelper(this);
    }

    public static class InitHelper {

        final ListContentView listContentView;

        int loadViewId;

        int swipeRefreshWrapperId;

        BaseListController listController;

        boolean requestData = true;

        private InitHelper(ListContentView listContentView) {
            this.listContentView = listContentView;
        }

        public InitHelper setLoadViewId(int loadViewId) {
            this.loadViewId = loadViewId;
            return this;
        }

        public InitHelper setSwipeRefreshWrapperId(int swipeRefreshWrapperId) {
            this.swipeRefreshWrapperId = swipeRefreshWrapperId;
            return this;
        }

        public InitHelper setListController(BaseListController listController) {
            this.listController = listController;
            return this;
        }

        public InitHelper setRequestData(boolean requestData) {
            this.requestData = requestData;
            return this;
        }

        public void init() {
            ILoadView loadView = null;
            ISwipeRefreshWrapper swipeRefreshWrapper = null;
            if (loadViewId > 0) {
                loadView = (ILoadView) listContentView.findViewById(loadViewId);
            }
            if (swipeRefreshWrapperId > 0) {
                swipeRefreshWrapper = (ISwipeRefreshWrapper) listContentView.findViewById(swipeRefreshWrapperId);
            }
            for (int i = 0; i < listContentView.getChildCount(); i++) {
                if (swipeRefreshWrapper != null && loadView != null) {
                    break;
                }
                View child = listContentView.getChildAt(i);
                if (child instanceof ISwipeRefreshWrapper) {
                    swipeRefreshWrapper = (ISwipeRefreshWrapper) child;
                } else if (child instanceof ILoadView) {
                    loadView = (ILoadView) child;
                }
            }

            listContentView.mSwipeRefreshWrapper = swipeRefreshWrapper;
            listController.getListManagerViewHolder().setLoadTipsView(loadView);
            listController.getListManagerViewHolder().setSwipeRefreshWrapper(swipeRefreshWrapper);
            listController.getListManagerViewHolder().setRecyclerListView(swipeRefreshWrapper.getRecyclerView());
            listContentView.mListController = listController;
            if (requestData) {
                listContentView.refreshData();
            }
        }
    }

}
