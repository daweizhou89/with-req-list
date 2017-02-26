package com.github.daweizhou89.reqlist.requester;

import android.app.Activity;
import android.os.SystemClock;
import android.support.annotation.CallSuper;

import com.github.daweizhou89.okhttpclientutils.TextResponseCallback;
import com.github.daweizhou89.reqlist.DebugLog;
import com.github.daweizhou89.reqlist.ReqList;
import com.github.daweizhou89.reqlist.manager.AbstractListManager;
import com.github.daweizhou89.reqlist.manager.RequestingTypeGenerator;
import com.github.daweizhou89.reqlist.model.ListItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daweizhou89 on 16/6/29.
 */
public abstract class AbstractRequester<LD extends Object> implements IRequester {

    public static final int DEFAULT_PAGE_SIZE = 20;

    /** 请求更多条件 */
    protected boolean mRequestingMoreAppend;
    /** 更多当前页 */
    protected int mPageNo;

    /** Activity */
    protected Activity mActivity;
    /** List Manager */
    protected AbstractListManager mListManager;
    /** data (List) */
    protected List<LD> mData;
    /** 请求类型 */
    protected int mRequestingType;

    /** 是否可以加载更多 */
    protected boolean mLoadMoreEnable;
    /** 记载更多的时间戳 */
    protected long mRequestMoreTimestamp;
    /** 加载跟多当前页码 */
    protected int mMorePageNo;
    protected int mPageSize = DEFAULT_PAGE_SIZE;

    /** 加载回调 */
    protected CallBack mCallBack = new CallBack();
    /** 加载更多回调 */
    protected MoreCallback mMoreCallback = new MoreCallback();
    /** ListItem中数据开始位置 */
    protected int mListItemPositionStart;
    /** 是否自身处理更多数据变化 */
    protected boolean mHandleMoreDataChanged;

    public AbstractRequester(AbstractListManager listManager) {
        this(listManager, false);
    }

    public AbstractRequester(AbstractListManager listManager, boolean loadMoreEnable) {
        this.mListManager = listManager;
        this.mRequestingType = RequestingTypeGenerator.getInstance().generateRequestingType();
        this.mActivity = listManager.getActivity();
        this.mLoadMoreEnable = loadMoreEnable;
        listManager.put(this.mRequestingType, this);
        initCacheData();
    }

    @Override
    public int getRequestingType() {
        return mRequestingType;
    }

    @Override
    public final void request(Object... inputs) {
        // 避免重复请求
        if (isRequesting()) {
            return;
        }
        setRequesting();
        mPageNo = 1;
        mListItemPositionStart = -1;
        onRequest(mPageNo, mCallBack, inputs);
    }

    @Override
    public final void requestMore(Object... inputs) {
        if (isRequesting()) {
            mListManager.onLoadMoreComplete();
            return;
        }
        if (mRequestingMoreAppend) {
            return;
        }

        mRequestingMoreAppend = true;
        mRequestMoreTimestamp = SystemClock.elapsedRealtime();
        final int morePageNo = mPageNo + 1;
        mMorePageNo = morePageNo;

        onRequestMore(morePageNo, mMoreCallback, inputs);
    }

    protected final void updatePageNoAfterMore() {
        mPageNo = mMorePageNo;
    }

    public final boolean isEffectiveTime(long timestamp) {
        return mListManager.isEffectiveTime(timestamp);
    }

    protected final int getFrag(int i, int rowCount) {
        if (i == 0) {
            return ListItem.FLAG_FIRST;
        } else if (i == rowCount - 1) {
            return ListItem.FLAG_LAST;
        }
        return ListItem.FLAG_NORMAL;
    }

    protected void initCacheData() {
        final String cacheKey = getCacheKey();
        if (cacheKey != null) {
            try {
                initCacheDataInternal(cacheKey);
                if (!isEmpty()) {
                    mListManager.sendUpdateMessage();
                }
            } catch (Exception e) {
                DebugLog.e(getClass(), "initCacheData", e);
            }
        }
    }

    protected void initCacheDataInternal(String cacheKey) {
        String cacheData = ReqList.getDataCacheManager().get(cacheKey);
        if (cacheData != null) {
            if (DebugLog.DEBUG) {
                DebugLog.d(getClass(), "initCacheDataInternal", cacheKey + "," + cacheData);
            }
            onResponse(cacheKey, cacheData);
        }
    }

    /***
     * 用于判断是否覆盖父类方法
     * @param clazz
     * @param name
     * @param parameterTypes
     * @return
     */
    private boolean isMethodOverride(Class clazz, String name, Class<?>... parameterTypes) {
        boolean find = false;
        try {
            find = clazz.getDeclaredMethod(name, parameterTypes).getDeclaringClass().equals(clazz);
        } catch (NoSuchMethodException e) {
            // TODO nothing
        } catch (Exception e) {
            DebugLog.e(getClass(), "isMethodOverride", e);
        }
        return find;
    }

    protected final boolean isRequesting() {
        return mListManager.isRequesting(mRequestingType);
    }

    protected final void setRequesting() {
        mListManager.setRequesting(mRequestingType);
    }

    protected final void afterRequesting() {
        mListManager.afterRequesting(mRequestingType);
    }

    protected final void sendUpdateMessage() {
        mListManager.sendUpdateMessage(mRequestingType);
    }

    public final void  addListItem(ListItem item) {
        mListManager.addListItem(item);
    }

    public List<LD> getData() {
        return mData;
    }

    public void setData(List<LD> data) {
        if (mLoadMoreEnable) {
            mListManager.getListManagerViewHolder().trySetLoadMoreEnable(data, getPageSize());
        }
        mData = data;
    }

    public void appendData(List<LD> data) {
        if (mLoadMoreEnable) {
            mListManager.getListManagerViewHolder().trySetLoadMoreEnable(data, getPageSize());
        }
        if (mData != null && data != null && !data.isEmpty()) {
            List<LD> removed = new ArrayList<LD>();
            for (int i = 0; i < data.size(); i++) {
                final LD item = data.get(i);
                if (mData.contains(item)) {
                    removed.add(item);
                }
            }
            data.removeAll(removed);
            mData.addAll(data);
            mHandleMoreDataChanged = buildAppendedData(data);
        }
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

    public MoreCallback getMoreCallback() {
        return mMoreCallback;
    }

    public void setLoadMoreEnable(boolean loadMoreEnable) {
        mLoadMoreEnable = loadMoreEnable;
    }

    public boolean isEmpty() {
        return mData == null || mData.isEmpty();
    }

    protected abstract void onResponse(String url, String response);

    protected void onResponseMore(String url, String response) {}

    protected void onResponseError(String url, Throwable throwable) {
        // TODO nothing
    }

    protected void onResponseErrorMore(String url, Throwable throwable) {
        // TODO nothing
    }

    @CallSuper
    public void onLoadMoreComplete() {
        mListManager.onLoadMoreComplete();
        mRequestingMoreAppend = false;
    }

    @Override
    public final void build(Object... inputs) {
        mListItemPositionStart = mListManager.getListItems().size();
        onBeforeBuild(inputs);
        onBuild(inputs);
        onAfterBuild(inputs);
    }

    public int getListItemPositionStart() {
        return mListItemPositionStart;
    }

    /***
     * 数据缓存key，需要缓存时覆盖
     * @return
     */
    protected String getCacheKey() {
        return null;
    }

    protected boolean isHandleMoreDataChanged() {
        return mHandleMoreDataChanged;
    }

    protected void onRequestMore(int morePageNo, MoreCallback callback, Object... inputs) {
        // TODO nothing
    }

    /***
     * 自身notifyDataChanged，不重新build
     * 返回true
     * @param data
     * @return
     */
    protected boolean buildAppendedData(List<LD> data) {
        return false;
    }

    protected abstract void onRequest(int pageNo, CallBack callback, Object... inputs);

    protected abstract void onBuild(Object... inputs);

    protected void onBeforeBuild(Object... inputs) {
        // TODO nothing
    }

    protected void onAfterBuild(Object... inputs) {
        // TODO nothing
    }

    public class CallBack implements TextResponseCallback {

        @Override
        public final void onSuccess(String url, String response) {
            String cacheKey = getCacheKey();
            if (cacheKey != null) {
                ReqList.getDataCacheManager().put(getCacheKey(), response);
            }
            onResponse(url, response);
            afterRequesting();
        }

        @Override
        public final void onFailure(String url, Throwable throwable) {
            if (DebugLog.DEBUG) {
                DebugLog.e(getClass(), "onFailure", url, throwable);
            }
            onResponseError(url, throwable);
            afterRequesting();
        }
    }

    public class MoreCallback implements TextResponseCallback {

        @Override
        public void onSuccess(String url, String response) {
            if (isEffectiveTime(mRequestMoreTimestamp)) {
                updatePageNoAfterMore();
                onResponseMore(url, response);
                if (!isHandleMoreDataChanged()) {
                    sendUpdateMessage();
                }
            }
            onLoadMoreComplete();
        }

        @Override
        public void onFailure(String url, Throwable throwable) {
            onResponseErrorMore(url, throwable);
            onLoadMoreComplete();
        }
    }
}
