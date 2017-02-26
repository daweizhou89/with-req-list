package com.github.daweizhou89.listview;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;


/**
 * Created by daweizhou89 on 16/3/18.
 */
public class SwipeRefreshListLayout extends SwipeRefreshLayout implements SwipeRefreshLayout.OnRefreshListener {

    protected RecyclerListView mRecyclerView;

    private OnListRefreshListener mOnListRefreshListener;

    public SwipeRefreshListLayout(Context context) {
        this(context, null);
    }

    public SwipeRefreshListLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    private void initViews() {
        int[] colors = SwipeRefreshListConfig.getSwipeColorSchemeColors();
        if (colors != null) {
            setColorSchemeColors(colors);
        }
        setOnRefreshListener(this);

        setupRecyclerView();
    }

    protected void setupRecyclerView() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        mRecyclerView = (RecyclerListView) layoutInflater.inflate(SwipeRefreshListConfig.getRecyclerListViewResources(), null);
        addView(mRecyclerView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    public RecyclerListView getRecyclerView() {
        return mRecyclerView;
    }

    public void setOnListRefreshListener(OnListRefreshListener onListRefreshListener) {
        mOnListRefreshListener = onListRefreshListener;
        mRecyclerView.setOnLoadMoreListener(onListRefreshListener);
    }

    public void addOnScrollListener(RecyclerView.OnScrollListener listener) {
        mRecyclerView.addOnScrollListener(listener);
    }

    public void removeOnScrollListener(RecyclerView.OnScrollListener listener) {
        mRecyclerView.removeOnScrollListener(listener);
    }

    /***
     * 有下一页数据时，打开／关闭加载更多
     * @param enable
     */
    public void setLoadMoreEnable(boolean enable) {
        mRecyclerView.setLoadMoreEnable(enable);
    }

    public void onRefresh() {
        if (mOnListRefreshListener != null) {
            mOnListRefreshListener.onBeforeRefresh();
            mOnListRefreshListener.onRefresh();
        }
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        mRecyclerView.setAdapter(adapter);
    }

    /***
     * －－重要－－
     * 数据请求完成后，须调用该方法
     */
    public void onRefreshComplete() {
        this.setRefreshing(false);
    }

    /***
     * －－重要－－
     * 加载更多完成后，须调用该方法
     */
    public void onLoadMoreComplete() {
        mRecyclerView.onLoadMoreComplete();
    }

    /**
     * 刷新／加载更多监听器
     * onLoadMore 在调用 setLoadMoreEnable(true)才有效
     */
    public interface OnListRefreshListener extends RecyclerListView.OnLoadMoreListener {
        void onBeforeRefresh();
        void onRefresh();
    }
}
