package com.github.daweizhou89.reqlist.loader;

import android.support.annotation.CallSuper;

import com.github.daweizhou89.reqlist.ReqListContext;
import com.github.daweizhou89.reqlist.controler.BaseListController;
import com.github.daweizhou89.reqlist.model.ListItem;
import com.github.daweizhou89.reqlist.model.LoadTypeGenerator;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by zhoudawei on 2017/3/25.
 */

public abstract class BaseLoader<LD, R> implements ILoader<R> {

    /** ReqListContext */
    protected ReqListContext mReqListContext;
    /** List Manager */
    protected BaseListController mListController;

    public static final int DEFAULT_PAGE_SIZE = 20;

    /** data (List) */
    protected List<LD> mData;
    /** 请求类型 */
    protected int mLoadType;
    /** 更多当前页 */
    protected int mPageNo;
    /** 请求页大小 */
    protected int mPageSize = DEFAULT_PAGE_SIZE;
    /** 是否可以加载更多 */
    protected boolean mLoadMore;
    /** 请求更多条件 */
    protected boolean mDuringLoadingMore;
    /** 记载更多的时间戳 */
    protected long mLoadMoreTimestamp;
    /** 加载跟多当前页码 */
    protected int mMorePageNo;
    /** ListItem中数据开始位置 */
    protected int mListItemPositionStart;
    /**  */
    protected Disposable mDisposable;

    public BaseLoader(BaseListController listController, boolean loadMore) {
        this.mListController = listController;
        this.mReqListContext = listController.getContextHolder();
        this.mLoadType = LoadTypeGenerator.getInstance().generateLoadType();
        this.mLoadMore = loadMore;
        listController.put(this.mLoadType, this);
    }

    public int getPageNo() {
        return mPageNo;
    }

    public void setPageSize(int pageSize) {
        this.mPageSize = pageSize;
    }

    public int getPageSize() {
        return mPageSize;
    }

    public int getListItemPositionStart() {
        return mListItemPositionStart;
    }

    public boolean isEmpty() {
        return mData == null || mData.isEmpty();
    }

    public void dispose() {
        mReqListContext.removeDisposable(mDisposable);
    }

    @Override
    public final boolean isLoading() {
        return mListController.isLoading(mLoadType);
    }

    public final void setLoading() {
        mListController.setLoading(mLoadType);
    }

    public final void afterLoading() {
        mListController.afterLoading(mLoadType);
        dispose();
    }

    public final void sendUpdateMessage() {
        mListController.sendUpdateMessage(mLoadType);
    }

    public final void  addListItem(ListItem item) {
        mReqListContext.addListItem(item);
    }

    public List<LD> getData() {
        return mData;
    }

    public void setData(List<LD> data) {
        if (mLoadMore) {
            mListController.getListManagerViewHolder().trySetLoadMoreEnable(data, getPageSize());
        }
        mData = data;
    }

    public void appendData(List<LD> data) {
        if (mLoadMore) {
            mListController.getListManagerViewHolder().trySetLoadMoreEnable(data, getPageSize());
        }
        if (mData != null && data != null && !data.isEmpty()) {
            List<LD> removed = new ArrayList<>();
            for (int i = 0; i < data.size(); i++) {
                final LD item = data.get(i);
                if (mData.contains(item)) {
                    removed.add(item);
                }
            }
            data.removeAll(removed);
            mData.addAll(data);
        }
    }

    public boolean isEffectiveTime() {
        return mListController.isEffectiveTime(mLoadMoreTimestamp);
    }

    public void updatePageNo() {
        mPageNo = mMorePageNo;
    }

    @CallSuper
    public void onLoadMoreComplete() {
        mListController.onLoadMoreComplete();
        mDuringLoadingMore = false;
    }

    @Override
    public int getLoadType() {
        return mLoadType;
    }

    @Override
    public void load() {
        load(false);
    }

    @Override
    public final void build(Object... inputs) {
        mListItemPositionStart = mReqListContext.getListItemCount();
        onBuild(inputs);
        if (mLoadMore) {
            mReqListContext.addLoadFooter();
        }
    }

    /***
     * 数据缓存key，需要缓存时覆盖
     * @return
     */
    public String getCacheKey() {
        return null;
    }

    protected abstract void onBuild(Object... inputs);

}
