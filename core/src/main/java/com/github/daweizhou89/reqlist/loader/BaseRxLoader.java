package com.github.daweizhou89.reqlist.loader;

import android.os.SystemClock;

import com.github.daweizhou89.reqlist.DebugLog;
import com.github.daweizhou89.reqlist.controler.BaseListController;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zhoudawei on 2017/3/27.
 */

public abstract class BaseRxLoader<LD, R> extends BaseLoader<LD, R> {

    public BaseRxLoader(BaseListController listController, boolean loadMore) {
        super(listController, loadMore);
    }

    @Override
    public void load(boolean more, Object... inputs) {
        if (!more) {
            loadFirst(inputs);
        } else {
            loadMore(inputs);
        }
    }

    private void loadFirst(Object... inputs) {
        // 避免重复请求
        if (isLoading()) {
            return;
        }
        setLoading();
        mPageNo = 1;
        mListItemPositionStart = -1;
        onSubscribe(onCreateObservable(mPageNo, false, inputs), false);
    }

    private void loadMore(Object... inputs) {
        if (isLoading()) {
            mListController.onLoadMoreComplete();
            return;
        }
        if (!mDuringLoadingMore) {
            mDuringLoadingMore = true;
            mLoadMoreTimestamp = SystemClock.elapsedRealtime();
            final int morePageNo = mPageNo + 1;
            mMorePageNo = morePageNo;
            onSubscribe(onCreateObservable(morePageNo, true, inputs), true);
        }
    }

    @Override
    public void onResponseError(Throwable throwable, boolean more) {
        // TODO nothing
    }

    public void onSubscribe(Observable<R> observable, final boolean more) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<R>() {
                    @Override
                    public void accept(R response) throws Exception {
                        onResponse(response, more);
                        BaseRxLoader.this.afterLoading();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (DebugLog.DEBUG) {
                            DebugLog.e(getClass(), "onSubscribe", "", throwable);
                        }
                        onResponseError(throwable, more);
                        BaseRxLoader.this.afterLoading();
                    }
                });
    }

    public abstract Observable<R> onCreateObservable(int pageNo, boolean more, Object... inputs);
}
