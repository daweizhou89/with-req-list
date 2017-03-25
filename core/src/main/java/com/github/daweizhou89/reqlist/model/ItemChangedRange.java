package com.github.daweizhou89.reqlist.model;

import com.github.daweizhou89.reqlist.adapter.BaseListAdapter;

/**
 * Created by zhoudawei on 2017/3/25.
 */
public class ItemChangedRange {
    public boolean changed;
    public int positionStart;
    public int itemCount;

    public boolean isToHandle(int listSize) {
        return changed
                && listSize > 0
                && positionStart < listSize
                && itemCount > 0;
    }

    public void handle(BaseListAdapter adapter) {
        adapter.notifyItemRangeChanged(positionStart, itemCount);
    }

    public void reset() {
        changed = false;
    }
}
