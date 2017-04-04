package com.github.daweizhou89.reqlist.loader.http;

import android.os.SystemClock;

import com.github.daweizhou89.reqlist.controler.BaseListController;
import com.github.daweizhou89.reqlist.loader.BaseLoader;

import io.reactivex.disposables.Disposable;

/**
 * Created by daweizhou89 on 16/6/29.
 */
public abstract class BaseHttpLoader<LD extends Object> extends BaseLoader<LD, String> {

    /** 加载回调 */
    protected ResponseCallBack mCallBack;
    /** 加载更多回调 */
    protected MoreResponseCallback mMoreCallback;
    /** 缓存辅助类 */
    protected CacheDataHelper mCacheDataHelper;

    public BaseHttpLoader(BaseListController listController, boolean loadMore) {
        super(listController, loadMore);
        mCacheDataHelper = new CacheDataHelper(this, listController);
        mCallBack = new ResponseCallBack(this);
        mMoreCallback = new MoreResponseCallback(this);
        initCacheData();
    }

    public final void initCacheData() {
        mCacheDataHelper.initCacheData();
    }

    @Override
    public final void load(boolean more, Object... inputs) {
        Disposable disposable;
        if (!more) {
            disposable = loadFirst(inputs);
        } else {
            disposable = loadMore(inputs);
        }
        if (disposable != null) {
            mDisposable = disposable;
            mReqListContext.addDisposable(disposable);
        }
    }

    private Disposable loadFirst(Object... inputs) {
        Disposable disposable = null;
        // 避免重复请求
        if (isLoading()) {
            return disposable;
        }
        setLoading();
        mPageNo = 1;
        mListItemPositionStart = -1;
        dispose();
        disposable = onLoad(mPageNo, false, mCallBack, inputs);
        return disposable;
    }

    private Disposable loadMore(Object... inputs) {
        Disposable disposable = null;
        if (isLoading()) {
            mListController.onLoadMoreComplete();
            return disposable;
        }
        if (!mDuringLoadingMore) {
            mDuringLoadingMore = true;
            mLoadMoreTimestamp = SystemClock.elapsedRealtime();
            final int morePageNo = mPageNo + 1;
            mMorePageNo = morePageNo;
            dispose();
            disposable = onLoad(morePageNo, true, mMoreCallback, inputs);
        }
        return disposable;
    }

    @Override
    public void onResponse(String response, boolean more) {
        // TODO nothing
    }

    @Override
    public void onResponseError(Throwable throwable, boolean more) {
        // TODO nothing
    }

    protected abstract Disposable onLoad(int pageNo, boolean more, ResponseCallBack callback, Object... inputs);

}
