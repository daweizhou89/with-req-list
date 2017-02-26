package com.github.daweizhou89.reqlist.manager;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.CallSuper;
import android.util.SparseArray;

import com.github.daweizhou89.listview.RecyclerListView;
import com.github.daweizhou89.listview.SwipeRefreshListLayout;
import com.github.daweizhou89.reqlist.Constant;
import com.github.daweizhou89.reqlist.DebugLog;
import com.github.daweizhou89.reqlist.adapter.AbstractListAdapter;
import com.github.daweizhou89.reqlist.model.ListItem;
import com.github.daweizhou89.reqlist.requester.IRequester;
import com.github.daweizhou89.reqlist.view.ILoadTips;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

/**
 *
 * Created by daweizhou89 on 16/5/9.
 */
public abstract class AbstractListManager<AT extends Activity> {

    /**
     * 循环更新消息延迟
     */
    protected final static long DELAY_REQUESTING_LOOP = 3 * Constant.TIME_ONE_MINUTE;

    /**
     * 需要重新加载的时间
     */
    protected final static long DELAY_NEED_REQUESTING = Constant.TIME_ONE_HOUR;

    /***
     * 最大的请求类型，大于0xFF是普通Handler消息
     */
    protected final static int MSG_MAX_REQUESTING_TYPE = 0xFF;

    /**
     * 强制build list
     */
    protected final static int MSG_FORCE_BUILD = 0x100;

    /**
     * 请求数据
     */
    protected final static int MSG_REQUEST = 0x101;

    /**
     * Activity
     */
    protected AT mActivity;
    /** 设定为private，getListAdapter时候初始化 */
    private AbstractListAdapter mListAdapter;
    /**  */
    private ListManagerViewHolder mListManagerViewHolder;

    /**  */
    private List<ListItem> mListItems = new ArrayList<>();

    /**  */
    private int mLastListItemsSize;
    /**  */
    private ItemChangedRange mItemChangedRange = new ItemChangedRange();
    /**  */
    private ItemInserted mItemInserted = new ItemInserted();

    /**
     * 所有请求，key：type
     */
    private SparseArray<IRequester> mRequester = new SparseArray<>();

    /**
     * 请求数据时间戳：过滤过期的更多数据
     */
    private long mRequestingDataTimestamp = -DELAY_NEED_REQUESTING;
    /**
     * 请求数据集合类型
     */
    private int[] mRequestingTypes;
    /**
     * 请求数据条件：统一构造首页数据
     */
    private final BitSet mRequestingDataSet = new BitSet();
    /***
     * onRestart时是否需要更新界面
     */
    private boolean mUpdateOnRestart;
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
                case MSG_REQUEST:
                    handleRequestDataInternal();
                    break;
                default:
                    tryBuildListAfterRequest(msg.what);
                    break;
            }
        }
    };

    private int mLoadedEmptyTips = ILoadTips.TIPS_LOADED_EMPTY;

    private String mEmptyTipsText;

    public AbstractListManager(Context context) {
        mActivity = (AT) context;
        mListManagerViewHolder = new ListManagerViewHolder(this);
    }

    public void put(int requestingType, IRequester requester) {
        mRequester.put(requestingType, requester);
    }

    public IRequester getRequester(int requestingType) {
        return mRequester.get(requestingType);
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
            DebugLog.v(getClass(), "tryBuildListAfterRequest", "requestingType:" + requestingType + "," + (mRequestingDataSet.isEmpty()));
            DebugLog.v(getClass(), "tryBuildListAfterRequest", "find:" + find);
        }
        if (find >= 0) {
            IRequester requester = mRequester.get(requestingType);
            mRequestingHandler.removeMessages(requestingType);
            if (mRequestingDataSet.isEmpty()) {
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
        Message.obtain(mRequestingHandler, MSG_REQUEST).sendToTarget();
    }

    private final void handleRequestDataInternal() {
        if (!isConnected(mActivity)) {
            mListManagerViewHolder.setLoadTips(ILoadTips.TIPS_LOADED_NO_NETWORK);
            return;
        }
        if (isEmpty()) {
            mListManagerViewHolder.setLoadTips(ILoadTips.TIPS_LOADING);
        }
        mRequestingDataTimestamp = SystemClock.elapsedRealtime();
        if (mListAdapter != null) {
            mListAdapter.setDataDirty();
        }
        onRequestData();
    }

    /***
     * 给Adapter给个获取数据的入口
     *
     * @return 列表元素
     */
    public List<ListItem> getListItems() {
        return mListItems;
    }

    /***
     * 给Adapter给个获取数据的入口
     *
     * @return 列表元素
     */
    public ListItem getListItem(int position) {
        return mListItems.get(position);
    }

    public void setListManagerViewHolder(ListManagerViewHolder listManagerViewHolder) {
        mListManagerViewHolder = listManagerViewHolder;
    }

    public ListManagerViewHolder getListManagerViewHolder() {
        return mListManagerViewHolder;
    }

    public RecyclerListView getRecyclerView() {
        return mListManagerViewHolder.getRecyclerView();
    }

    public SwipeRefreshListLayout getSwipeRefreshListLayout() {
        return mListManagerViewHolder.getSwipeRefreshListLayout();
    }

    public void addListItem(ListItem item) {
        mListItems.add(item);
    }

    public Activity getActivity() {
        return mActivity;
    }

    /***
     * @return 是否为空
     */
    public boolean isEmpty() {
        return mListItems.isEmpty();
    }

    public int getLastListItemsSize() {
        return mLastListItemsSize;
    }

    private void buildListInternal() {
        mLastListItemsSize = mListItems.size();
        mListItems.clear();
        mListManagerViewHolder.onRefreshComplete();
        this.onBuildList();
        tryAddLoadMoreItem();
        //如果网络获取下来没有数据的话则对应提示
        if (this.isEmpty()) {
            if (!isRequesting()) {
                mListManagerViewHolder.setEmptyTipsText(getEmptyTipsText());
                mListManagerViewHolder.setLoadTips(getLoadedEmptyTips());
            }
        } else {
            mListManagerViewHolder.setLoadTips(ILoadTips.TIPS_LOADED_OK);
        }
        if (mItemChangedRange.isToHandle(mListItems.size())) {
            if (DebugLog.DEBUG) {
                DebugLog.d(getClass(), "buildListInternal", "mItemChangedRange:" + mItemChangedRange.positionStart + "," + mItemChangedRange.itemCount);
            }
            mItemChangedRange.handle(mListAdapter);
        } else if (mItemInserted.isToHandle(mListItems.size())) {
            if (DebugLog.DEBUG) {
                DebugLog.d(getClass(), "buildListInternal", "mItemInserted:" + mItemInserted.position);
            }
            mItemInserted.handle(mListAdapter);
        } else {
            if (DebugLog.DEBUG) {
                DebugLog.d(getClass(), "buildListInternal", "notifyDataSetChanged");
            }
            mListAdapter.notifyDataSetChanged();
        }
        mItemChangedRange.reset();
        mItemInserted.reset();
    }

    public void setItemChangedRange(int positionStart, int itemCount) {
        mItemChangedRange.positionStart = positionStart;
        mItemChangedRange.itemCount = itemCount;
        mItemChangedRange.changed = true;
    }

    public void setItemInserted(int position) {
        mItemInserted.position = position;
        mItemInserted.changed = true;
    }

    @CallSuper
    protected boolean isAddLoadMoreItem() {
        return !isEmpty() && mListAdapter instanceof AbstractListAdapter.INormalLoadMore;
    }

    /***
     * 添加加载更多项
     */
    private void tryAddLoadMoreItem() {
        if (isAddLoadMoreItem()) {
            final ListItem listItem = new ListItem(ListItem.TYPE_LOAD_MORE, null);
            addListItem(listItem);
        }
    }

    protected void onBeforeRefresh() {
        // TODO nothing
    }

    @CallSuper
    public void onLoadMoreComplete() {
        mListManagerViewHolder.onLoadMoreComplete();
        if (mListAdapter instanceof AbstractListAdapter.INormalLoadMore) {
            ((AbstractListAdapter.INormalLoadMore) mListAdapter).onLoadMoreComplete();
        }
    }

    @CallSuper
    public void setAdapterDataDirty() {
        if (mListAdapter != null) {
            mListAdapter.setDataDirty();
        }
    }

    public final boolean isEffectiveTime(long timestamp) {
        return mRequestingDataTimestamp < timestamp;
    }

    public final boolean isRequesting() {
        return !mRequestingDataSet.isEmpty();
    }

    public final boolean isRequesting(int requestingType) {
        return mRequestingDataSet.get(requestingType);
    }

    public final void setRequesting(int requestingType) {
        mRequestingDataSet.set(requestingType);
    }

    public final void afterRequesting(int requestingType) {
        if (DebugLog.DEBUG) {
            DebugLog.d(getClass(), "afterRequesting", "requestingType:" + requestingType);
        }
        mRequestingDataSet.clear(requestingType);
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

    public final void post(Runnable runnable) {
        mRequestingHandler.post(runnable);
    }

    /***
     * onRestart时更新逻辑
     * 前提需要setUpdateOnRestart(true)
     */
    protected void updateOnRestart() {
        // TODO nothing
    }

    /***
     * 需要在onRestart时更新，设true
     * @param updateOnRestart
     */
    public void setUpdateOnRestart(boolean updateOnRestart) {
        mUpdateOnRestart = updateOnRestart;
    }

    /**
     * 没事不要覆盖这
     *
     * @return 是否需要刷新
     */
    public boolean needRequesting() {
        return SystemClock.elapsedRealtime() - mRequestingDataTimestamp > DELAY_NEED_REQUESTING;
    }

    public void clear() {
        mListItems.clear();
        if (mListAdapter != null) {
            mListAdapter.notifyDataSetChanged();
        }
    }

    /***
     * 子类不覆盖，返回默认
     *
     * @return 定时请求时间
     */
    protected long getTimingRequestingTime() {
        return DELAY_REQUESTING_LOOP;
    }

    /***
     * 子类不覆盖，返回默认
     *
     * @return 内容为空的提示类型
     */
    protected int getLoadedEmptyTips() {
        return mLoadedEmptyTips;
    }

    /***
     * 自定义的内容列表空提示
     *
     * @return
     */
    protected String getEmptyTipsText() {
        return mEmptyTipsText;
    }

    public void setEmptyTipsText(String emptyTipsText) {
        this.mEmptyTipsText = emptyTipsText;
    }

    public void setLoadedEmptyTips(int loadedEmptyTips) {
        this.mLoadedEmptyTips = loadedEmptyTips;
    }

    /***
     * 获取请求类型
     * @return
     */
    protected int[] getRequestingTypes() {
        if (mRequestingTypes == null || mRequestingTypes.length != mRequester.size()) {
            int[] requestTypes = new int[mRequester.size()];
            for (int i = 0; i < mRequester.size(); i++) {
                requestTypes[i] = mRequester.get(mRequester.keyAt(i)).getRequestingType();
            }
            Arrays.sort(requestTypes);
            mRequestingTypes = requestTypes;
        }
        return mRequestingTypes;
    }

    protected final AbstractListAdapter getListAdapter() {
        if (mListAdapter == null) {
            mListAdapter = onCreateListAdapter();
        }
        return mListAdapter;
    }

    /***
     * call by #getListAdapter
     * @return
     */
    protected abstract AbstractListAdapter onCreateListAdapter();

    protected abstract void onBuildList();

    protected abstract void onRequestData();

    protected abstract void onRequestMoreAppend();

    /**
     * 检查当前是否连接
     *
     * @param context
     * @return true表示当前网络处于连接状态，否则返回false
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            return true;
        }
        return false;
    }

    private static class ItemChangedRange {
        boolean changed;
        int positionStart;
        int itemCount;

        public boolean isToHandle(int listSize) {
            return changed
                    && listSize > 0
                    && positionStart < listSize
                    && itemCount > 0;
        }

        public void handle(AbstractListAdapter adapter) {
            adapter.notifyItemRangeChanged(positionStart, itemCount);
        }

        public void reset() {
            changed = false;
        }
    }

    private static class ItemInserted {
        boolean changed;
        int position;

        public boolean isToHandle(int listSize) {
            return changed
                    && listSize > 0
                    && position < listSize;
        }

        public void handle(AbstractListAdapter adapter) {
            adapter.notifyItemInserted(position);
        }

        public void reset() {
            changed = false;
        }
    }

}
