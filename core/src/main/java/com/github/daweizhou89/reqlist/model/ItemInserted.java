package com.github.daweizhou89.reqlist.model;

import com.github.daweizhou89.reqlist.adapter.BaseListAdapter;

/**
 * Created by zhoudawei on 2017/3/25.
 */
public class ItemInserted {
    public boolean changed;
    public int position;

    public boolean isToHandle(int listSize) {
        return changed
                && listSize > 0
                && position < listSize;
    }

    public void handle(BaseListAdapter adapter) {
        adapter.notifyItemInserted(position);
    }

    public void reset() {
        changed = false;
    }
}
