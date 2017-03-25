package com.github.daweizhou89.reqlist.controler;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.CallSuper;
import android.util.SparseArray;

import com.github.daweizhou89.listview.RecyclerListView;
import com.github.daweizhou89.reqlist.Constant;
import com.github.daweizhou89.reqlist.DebugLog;
import com.github.daweizhou89.reqlist.ReqListContext;
import com.github.daweizhou89.reqlist.adapter.BaseListAdapter;
import com.github.daweizhou89.reqlist.loader.ILoader;
import com.github.daweizhou89.reqlist.model.LoadTypeGenerator;
import com.github.daweizhou89.reqlist.util.NetUtil;
import com.github.daweizhou89.reqlist.view.ILoadView;

import java.util.Arrays;
import java.util.BitSet;

/**
 *
 * Created by daweizhou89 on 16/5/9.
 */
public abstract class BaseListController {

    /**
     * 需要重新加载的时间
     */
    protected final static long DELAY_NEED_LOAD = Constant.TIME_ONE_HOUR;

    /**
     * 强制build list
     */
    protected final static int MSG_FORCE_BUILD = LoadTypeGenerator.MSG_MAX_REQUESTING_TYPE + 1;

    /**
     * 请求数据
     */
    protected final static int MSG_LOAD = LoadTypeGenerator.MSG_MAX_REQUESTING_TYPE + 2;

    /** 上下文 */
    private ReqListContext mReqListContext;
    /** 设定为private，getListAdapter时候初始化 */
    private BaseListAdapter mListAdapter;
    /**  */
    private ListControllerViewHolder mListControllerViewHolder;

    /**
     * 所有请求，key：type
     */
    private SparseArray<ILoader> mLoaders = new SparseArray<>();

    /**
     * 请求数据时间戳：过滤过期的更多数据
     */
    private long mLoadDataTimestamp = -DELAY_NEED_LOAD;
    /**
     * 请求数据集合类型
     */
    private int[] mRequestingTypes;
    /**
     * 请求数据条件：统一构造首页数据
     */
    private final BitSet mLoadingDataSet = new BitSet();
    /**
     * 是否等待所有请求回来才刷新
     */
    private boolean mWaitAllRequest = false;
    /**
     * 用来同步单个请求完成消息
     */
    private final Handler mRequestingHandler = new Handler() {

        @Override
        public void handleMessage(android.os.Message msg) {
            if (DebugLog.DEBUG) {
                DebugLog.v(getClass(), "handleMessage", "msg.what:" + msg.what);
            }
            switch (msg.what) {
                case MSG_FORCE_BUILD:
                    mRequestingHandler.removeMessages(msg.what);
                    buildListInternal();
                    break;
                case MSG_LOAD:
                    handleRequestDataInternal();
                    break;
                default:
                    tryBuildListAfterRequest(msg.what);
                    break;
            }
        }
    };

    public BaseListController(ReqListContext reqListContext) {
        mReqListContext = reqListContext;
        mListControllerViewHolder = new ListControllerViewHolder(this);
    }

    public void put(int requestingType, ILoader requester) {
        mLoaders.put(requestingType, requester);
    }

    public ILoader getRequester(int requestingType) {
        return mLoaders.get(requestingType);
    }

    public void setWaitAllRequest(boolean waitAllRequest) {
        this.mWaitAllRequest = waitAllRequest;
    }

    /***
     * 请求后尝试构造列表
     *
     * @param requestingType
     */
    private void tryBuildListAfterRequest(int requestingType) {
        // 清除请求类型
        int[] types = getRequestingTypes();
        // 所有请求标记都清空后，构造列表（构造完成会刷新界面）
        int find = Arrays.binarySearch(types, requestingType);
        if (DebugLog.DEBUG) {
            DebugLog.v(getClass(), "tryBuildListAfterRequest", "types:" + Arrays.toString(types));
            DebugLog.v(getClass(), "tryBuildListAfterRequest", "requestingType:" + requestingType + "," + (mLoadingDataSet.isEmpty()));
            DebugLog.v(getClass(), "tryBuildListAfterRequest", "find:" + find);
        }
        if (find >= 0) {
            ILoader requester = mLoaders.get(requestingType);
            mRequestingHandler.removeMessages(requestingType);
            if (mLoadingDataSet.isEmpty()) {
                for (int type : types) {
                    mRequestingHandler.removeMessages(type);
                }
                sendUpdateMessage();
            } else if (!mWaitAllRequest
                    && requester != null
                    && !requester.isEmpty()) {
                sendUpdateMessage();
            }
        }
    }

    /**
     * 请求数据
     *
     * @return
     */
    public final void requestData() {
        Message.obtain(mRequestingHandler, MSG_LOAD).sendToTarget();
    }

    private final void handleRequestDataInternal() {
        if (!NetUtil.isConnected(mReqListContext.getContext())) {
            mListControllerViewHolder.setLoadState(ILoadView.STATE_LOADED_NO_NETWORK);
            return;
        }
        if (mReqListContext.isEmpty()) {
            mListControllerViewHolder.setLoadState(ILoadView.STATE_LOADING);
        }
        mLoadDataTimestamp = SystemClock.elapsedRealtime();
        onRequestData(false);
    }

    public ListControllerViewHolder getListManagerViewHolder() {
        return mListControllerViewHolder;
    }

    public RecyclerListView getRecyclerView() {
        return mListControllerViewHolder.getRecyclerView();
    }

    public ReqListContext getContextHolder() {
        return mReqListContext;
    }

    private void buildListInternal() {
        mReqListContext.putValue(ReqListContext.LAST_LIST_ITEMS_SIZE, mReqListContext.getListItemCount());
        mReqListContext.clearListItem();
        mListControllerViewHolder.onRefreshComplete();
        this.onBuildList();
        //如果网络获取下来没有数据的话则对应提示
        if (mReqListContext.isEmpty()) {
            if (!isLoading()) {
                mListControllerViewHolder.setLoadState(ILoadView.STATE_LOADED_EMPTY);
            }
        } else {
            mListControllerViewHolder.setLoadState(ILoadView.STATE_LOADED_OK);
        }

        mReqListContext.notifyDataSetChanged(getListAdapter());
    }

    protected void onBeforeRefresh() {
        // TODO nothing
    }

    @CallSuper
    public void onLoadMoreComplete() {
        mListControllerViewHolder.onLoadMoreComplete();
        getListAdapter().onLoadMoreComplete();
    }

    public final boolean isEffectiveTime(long timestamp) {
        return mLoadDataTimestamp < timestamp;
    }

    public final boolean isLoading() {
        return !mLoadingDataSet.isEmpty();
    }

    public final boolean isLoading(int loadType) {
        return mLoadingDataSet.get(loadType);
    }

    public final void setLoading(int requestingType) {
        mLoadingDataSet.set(requestingType);
    }

    public final void afterLoading(int requestingType) {
        if (DebugLog.DEBUG) {
            DebugLog.d(getClass(), "afterLoading", "requestingType:" + requestingType);
        }
        mLoadingDataSet.clear(requestingType);
        mRequestingHandler.sendEmptyMessage(requestingType);
    }

    public final void sendUpdateMessage(int requestingType) {
        mRequestingHandler.sendEmptyMessage(requestingType);
    }

    /***
     * 构建列表
     */
    public final void sendUpdateMessage() {
        mRequestingHandler.sendEmptyMessage(MSG_FORCE_BUILD);
    }

    /***
     * post Runnable on mRequestingHandler
     */
    public final void post(Runnable runnable) {
        mRequestingHandler.post(runnable);
    }

    public boolean isEmpty() {
        return mReqListContext.isEmpty();
    }

    /**
     *
     * @return 是否需要刷新
     */
    public boolean needLoad() {
        return SystemClock.elapsedRealtime() - mLoadDataTimestamp > DELAY_NEED_LOAD;
    }

    /***
     * 获取请求类型
     * @return
     */
    protected int[] getRequestingTypes() {
        if (mRequestingTypes == null || mRequestingTypes.length != mLoaders.size()) {
            int[] requestTypes = new int[mLoaders.size()];
            for (int i = 0; i < mLoaders.size(); i++) {
                requestTypes[i] = mLoaders.get(mLoaders.keyAt(i)).getLoadType();
            }
            Arrays.sort(requestTypes);
            mRequestingTypes = requestTypes;
        }
        return mRequestingTypes;
    }

    protected final BaseListAdapter getListAdapter() {
        if (mListAdapter == null) {
            mListAdapter = mReqListContext.getAdapter();
            mListAdapter.setListController(this);
        }
        return mListAdapter;
    }

    protected abstract void onBuildList();

    protected abstract void onRequestData(boolean more);

}
