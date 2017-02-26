package com.github.daweizhou89.reqlist.holder;

import android.databinding.ViewDataBinding;
import android.support.annotation.CallSuper;
import android.view.View;

import com.github.daweizhou89.reqlist.adapter.AbstractListAdapter;

/**
 * Created by daweizhou89 on 2016/11/14.
 */
public abstract class AbstractListViewHolder<VDB extends ViewDataBinding> extends AbstractViewHolder<VDB> implements View.OnClickListener {

    protected AbstractListAdapter mAdapter;

    public AbstractListViewHolder(AbstractListAdapter adapter, View itemView) {
        super(itemView);
        this.mAdapter = adapter;
    }

    public AbstractListViewHolder(AbstractListAdapter adapter, VDB binding) {
        super(binding);
        this.mAdapter = adapter;
    }

    @Override
    @CallSuper
    public void onClick(View v) {
        onBeforeClick(v);
        onClickInternal(v);
        onAfterClick(v);
    }

    public void onBeforeClick(View v) {
        // TODO nothing
    }

    public void onAfterClick(View v) {
        // TODO nothing
    }

    public void onClickInternal(View v) {
        // TODO nothing
    }

}
