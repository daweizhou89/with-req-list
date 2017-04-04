package com.github.daweizhou89.reqlist;

import android.content.Context;

import com.github.daweizhou89.reqlist.adapter.BaseListAdapter;
import com.github.daweizhou89.reqlist.adapter.ILoadFooterCreator;
import com.github.daweizhou89.reqlist.model.ItemChangedRange;
import com.github.daweizhou89.reqlist.model.ItemInserted;
import com.github.daweizhou89.reqlist.model.ListItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by zhoudawei on 2017/3/24.
 */

public class ReqListContext {

    public static final String LAST_LIST_ITEMS_SIZE = "lastListItemsSize";

    /**  */
    private final Context mContext;
    /**  */
    private final BaseListAdapter mAdapter;
    /**  */
    private String mLoadingMoreText;
    /**  */
    private String mNoMoreText;
    /**  */
    private List<ListItem> mListItems = new ArrayList<>();
    /**  */
    private HashMap<String, Object> mValues = new HashMap<>();
    /**  */
    private ItemChangedRange mItemChangedRange = new ItemChangedRange();
    /**  */
    private ItemInserted mItemInserted = new ItemInserted();
    /**  */
    private LinkedList<Disposable> mDisposables = new LinkedList<>();

    private ReqListContext(Context context, BaseListAdapter adapter) {
        this.mContext = context;
        this.mAdapter = adapter;
    }

    public Context getContext() {
        return mContext;
    }

    public BaseListAdapter getAdapter() {
        return mAdapter;
    }

    public String getLoadingMoreText() {
        return mLoadingMoreText;
    }

    public String getNoMoreText() {
        return mNoMoreText;
    }

    public void putValue(String key, Object value) {
        mValues.put(key, value);
    }

    public <V> V getValue(String key) {
        return (V) mValues.get(key);
    }

    public List<ListItem> getListItems() {
        return mListItems;
    }

    public ListItem getListItem(int position) {
        return mListItems.get(position);
    }

    public void addListItem(ListItem item) {
        mListItems.add(item);
    }

    public int getListItemCount() {
        return mListItems.size();
    }

    public void clearListItem() {
        mListItems.clear();
    }

    public boolean isEmpty() {
        return mListItems.isEmpty();
    }

    public void addLoadFooter() {
        if (!isEmpty() && mAdapter instanceof ILoadFooterCreator) {
            final ListItem listItem = new ListItem(ListItem.Type.LOAD_FOOTER, null);
            addListItem(listItem);
        }
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

    public void notifyDataSetChanged(BaseListAdapter listAdapter) {
        int listItemCount = getListItemCount();
        if (mItemChangedRange.isToHandle(listItemCount)) {
            if (DebugLog.DEBUG) {
                DebugLog.d(getClass(), "notifyDataSetChanged", "mItemChangedRange:" + mItemChangedRange.positionStart + "," + mItemChangedRange.itemCount);
            }
            mItemChangedRange.handle(listAdapter);
        } else if (mItemInserted.isToHandle(listItemCount)) {
            if (DebugLog.DEBUG) {
                DebugLog.d(getClass(), "notifyDataSetChanged", "mItemInserted:" + mItemInserted.position);
            }
            mItemInserted.handle(listAdapter);
        } else {
            if (DebugLog.DEBUG) {
                DebugLog.d(getClass(), "notifyDataSetChanged", "notifyDataSetChanged");
            }
            listAdapter.notifyDataSetChanged();
        }
        mItemChangedRange.reset();
        mItemInserted.reset();
    }

    public void addDisposable(Disposable disposable) {
        if (disposable == null) {
            return;
        }
        if (!mDisposables.contains(disposable)) {
            mDisposables.add(disposable);
        }
    }

    public void removeDisposable(Disposable disposable) {
        if (disposable == null) {
            return;
        }
        if (!disposable.isDisposed()) {
            disposable.dispose();
        }
        mDisposables.remove(disposable);
    }

    public void dispose() {
        for (Disposable disposable : mDisposables) {
            if (!disposable.isDisposed()) {
                disposable.dispose();
            }
        }
        mDisposables.clear();
    }

    /***
     * builder
     */
    public static class Builder {

        Context context;
        BaseListAdapter adapter;
        String loadingMoreText;
        String noMoreText;

        public Builder(Context context, BaseListAdapter adapter) {
            this.context = context;
            this.adapter = adapter;
            loadingMoreText = context.getString(R.string.wrl_loading_more);
            noMoreText = context.getString(R.string.wrl_no_more);
        }

        public ReqListContext build() {
            ReqListContext reqListContext = new ReqListContext(context, adapter);
            reqListContext.mLoadingMoreText = loadingMoreText;
            reqListContext.mNoMoreText = noMoreText;
            return reqListContext;
        }

    }

}
