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

    private BaseLoadFooterHolder mLoadFooterHolder;

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

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (holder instanceof BaseLoadFooterHolder) {
            mLoadFooterHolder = (BaseLoadFooterHolder) holder;
        }
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (mLoadFooterHolder == holder) {
            mLoadFooterHolder = null;
        }
    }

    @Override
    public void onLoadFooterStateChanged(State state) {
        if (mLoadFooterHolder != null) {
            mLoadFooterHolder.onStateChanged(state);
        }
    }

    protected abstract BaseLoadFooterHolder onCreateLoadViewHolder(ViewGroup parent);

}
