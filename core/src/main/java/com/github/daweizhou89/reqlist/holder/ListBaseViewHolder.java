package com.github.daweizhou89.reqlist.holder;

import android.databinding.ViewDataBinding;
import android.view.View;

import com.github.daweizhou89.reqlist.adapter.BaseListAdapter;

/**
 * Created by daweizhou89 on 2016/11/14.
 */
public abstract class ListBaseViewHolder<VDB extends ViewDataBinding> extends ViewBaseHolder<VDB> {

    protected BaseListAdapter mAdapter;

    public ListBaseViewHolder(BaseListAdapter adapter, View itemView) {
        super(itemView);
        this.mAdapter = adapter;
    }

    public ListBaseViewHolder(BaseListAdapter adapter, VDB binding) {
        super(binding);
        this.mAdapter = adapter;
    }

}
