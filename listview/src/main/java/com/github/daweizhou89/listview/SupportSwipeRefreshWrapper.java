package com.github.daweizhou89.listview;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;


/**
 * Created by daweizhou89 on 16/3/18.
 */
public class SupportSwipeRefreshWrapper extends SwipeRefreshLayout implements ISwipeRefreshWrapper, SwipeRefreshLayout.OnRefreshListener {

    private static int[] sSwipeColorSchemeColors;

    private static int sRecyclerListViewResources = R.layout.recycler_list_view;

    protected RecyclerListView mRecyclerView;

    private OnListRefreshListener mOnListRefreshListener;

    public SupportSwipeRefreshWrapper(Context context) {
        this(context, null);
    }

    public SupportSwipeRefreshWrapper(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    private void initViews() {
        int[] colors = sSwipeColorSchemeColors;
        if (colors != null) {
            setColorSchemeColors(colors);
        }
        setOnRefreshListener(this);

        setupRecyclerView();
    }

    protected void setupRecyclerView() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        mRecyclerView = (RecyclerListView) layoutInflater.inflate(sRecyclerListViewResources, null);
        addView(mRecyclerView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    @Override
    public RecyclerListView getRecyclerView() {
        return mRecyclerView;
    }

    @Override
    public void setOnListRefreshListener(OnListRefreshListener onListRefreshListener) {
        mOnListRefreshListener = onListRefreshListener;
        mRecyclerView.setOnLoadMoreListener(onListRefreshListener);
    }

    @Override
    public void setLoadMoreEnable(boolean enable) {
        mRecyclerView.setLoadMoreEnable(enable);
    }

    public void onRefresh() {
        if (mOnListRefreshListener != null) {
            mOnListRefreshListener.onBeforeRefresh();
            mOnListRefreshListener.onRefresh();
        }
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onRefreshComplete() {
        this.setRefreshing(false);
    }

    @Override
    public void onLoadMoreComplete() {
        mRecyclerView.onLoadMoreComplete();
    }

    public static Config customize() {
        return new Config();
    }

    public static class Config {

        int[] swipeColorSchemeColors;

        int recyclerListViewResources;

        private Config() {
        }

        public Config setSwipeColorSchemeColors(int[] swipeColorSchemeColors) {
            this.swipeColorSchemeColors = swipeColorSchemeColors;
            return this;
        }

        public Config setRecyclerListViewResources(int recyclerListViewResources) {
            this.recyclerListViewResources = recyclerListViewResources;
            return this;
        }

        public void init() {
            sSwipeColorSchemeColors = swipeColorSchemeColors;
            if (recyclerListViewResources > 0) {
                sRecyclerListViewResources = recyclerListViewResources;
            }
        }
    }
}
