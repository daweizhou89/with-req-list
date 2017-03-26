package com.github.daweizhou89.reqlist.adapter;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.github.daweizhou89.reqlist.holder.BaseLoadFooterHolder;
import com.github.daweizhou89.reqlist.model.ListItem;

/**
 * Created by daweizhou89 on 16/10/26.
 */
public abstract class BaseLoadFooterListAdapter extends BaseListAdapter implements ILoadFooterCreator {

    public BaseLoadFooterListAdapter(Context context) {
        super(context);
    }

    @Override
    @CallSuper
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ListItem.Type.LOAD_FOOTER) {
            return onCreateLoadViewHolder(parent);
        }
        return onCreateViewHolderII(parent, viewType);
    }

    protected abstract BaseLoadFooterHolder onCreateLoadViewHolder(ViewGroup parent);

}
