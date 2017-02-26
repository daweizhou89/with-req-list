package com.github.daweizhou89.listview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by daweizhou89 on 16/8/19.
 */
public class RecyclerListView extends RecyclerView {

    /** 加载更多方式:预先加载 */
    public static final int LOAD_MORE_MODE_ADVANCE = 0;

    /** 加载更多方式:加载更多项 */
    public static final int LOAD_MORE_MODE_NORMAL = 1;

    /***
     * ［加载更多］列表显示剩余数量阀值
     * 列表显示剩余数量 小于或等于 阀值时，调用加载更多
     */
    public static final int DEFAULT_VISIBLE_THRESHOLD = 3;

    /***
     * ［加载更多］滑动监听
     */
    private RecyclerView.OnScrollListener mLoadMoreScrollListener;

    private OnLoadMoreListener mOnLoadMoreListener;

    private GridLayoutManager mGridLayoutManager;

    /*** 加载更多模式 */
    private int mLoadMoreMode = LOAD_MORE_MODE_ADVANCE;

    /*** 正在加载更多的条件 */
    private boolean mLoadingMore;

    /*** 是否可以加载更多 */
    private boolean mLoadMoreEnable;

    /***
     * ［加载更多］列表显示剩余数量阀值
     * 列表显示剩余数量 小于或等于 阀值时，调用加载更多
     */
    private int mVisibleThreshold = DEFAULT_VISIBLE_THRESHOLD;

    public RecyclerListView(Context context) {
        this(context, null);
    }

    public RecyclerListView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerListView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, android.support.v7.recyclerview.R.styleable.RecyclerView, defStyle, 0);
        int orientation = VERTICAL;
        int spanCount = 1;
        if (typedArray != null) {
            try {
                orientation = typedArray.getInteger(android.support.v7.recyclerview.R.styleable.RecyclerView_android_orientation, orientation);
                spanCount = typedArray.getInteger(android.support.v7.recyclerview.R.styleable.RecyclerView_spanCount, spanCount);
            } finally {
                typedArray.recycle();
            }
        }
        mGridLayoutManager = new GridLayoutManager(getContext(), spanCount, orientation, false);
        this.setHasFixedSize(true);
        this.setLayoutManager(mGridLayoutManager);
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        mOnLoadMoreListener = onLoadMoreListener;
    }

    /***
     * 有下一页数据时，打开／关闭加载更多
     * @param enable
     */
    public void setLoadMoreEnable(boolean enable) {
        switch (mLoadMoreMode) {
            case LOAD_MORE_MODE_ADVANCE:
                setLoadMoreEnableAdvance(enable);
                break;
            case LOAD_MORE_MODE_NORMAL:
                break;
        }
        mLoadMoreEnable = enable;
    }

    private void setLoadMoreEnableAdvance(boolean enable) {
        if (enable) {
            if (mLoadMoreScrollListener == null) {
                mLoadMoreScrollListener = new RecyclerView.OnScrollListener() {

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        if (recyclerView.getAdapter() == null || recyclerView.getAdapter().getItemCount() == 0) {
                            return;
                        }
                        final int totalItemCount = recyclerView.getAdapter().getItemCount();
                        final int lastVisibleItem = mGridLayoutManager.findLastCompletelyVisibleItemPosition();
                        // 预加载数据
                        if (!mLoadingMore && totalItemCount <= (lastVisibleItem + mVisibleThreshold)) {
                            performLoadMore();
                        }
                    }
                };
                addOnScrollListener(mLoadMoreScrollListener);
            }
        } else {
            removeOnScrollListener(mLoadMoreScrollListener);
            mLoadMoreScrollListener = null;
        }
    }

    public void performLoadMore() {
        if (mOnLoadMoreListener == null) {
            mLoadingMore = false;
        } else if (!mLoadingMore) {
            post(new Runnable() {
                @Override
                public void run() {
                    mOnLoadMoreListener.onLoadMore();
                }
            });
            mLoadingMore = true;
        }
    }

    public boolean isLoadingMore() {
        return mLoadingMore;
    }

    public boolean isLoadMoreEnable() {
        return mLoadMoreEnable;
    }

    public void setLoadMoreMode(int loadMoreMode) {
        this.mLoadMoreMode = loadMoreMode;
    }

    public int getLoadMoreMode() {
        return mLoadMoreMode;
    }

    public boolean isNormalLoadMoreMode() {
        return mLoadMoreMode == LOAD_MORE_MODE_NORMAL;
    }

    public boolean isAdvanceLoadMoreMode() {
        return mLoadMoreMode == LOAD_MORE_MODE_ADVANCE;
    }

    /***
     * －－重要－－
     * 加载更多完成后，须调用该方法
     */
    public void onLoadMoreComplete() {
        mLoadingMore = false;
        RecyclerView.Adapter adapter = this.getAdapter();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 加载更多监听器
     * onLoadMore 在调用 setLoadMoreEnable(true)才有效
     */
    public interface OnLoadMoreListener {
        void onLoadMore();
    }

}
