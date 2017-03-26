package com.github.daweizhou89.reqlist.holder;

import android.databinding.ViewDataBinding;
import android.view.View;

import com.github.daweizhou89.listview.RecyclerListView;
import com.github.daweizhou89.reqlist.adapter.BaseListAdapter;
import com.github.daweizhou89.reqlist.adapter.ILoadFooterCreator;
import com.github.daweizhou89.reqlist.holder.ListBaseViewHolder;

/**
 * Created by zhoudawei on 2017/3/26.
 */
public abstract class BaseLoadFooterHolder<VDB extends ViewDataBinding> extends ListBaseViewHolder<VDB> {

    public BaseLoadFooterHolder(BaseListAdapter adapter, View itemView) {
        super(adapter, itemView);
    }

    public BaseLoadFooterHolder(BaseListAdapter adapter, VDB binding) {
        super(adapter, binding);
    }

    @Override
    public final void bindData(int position) {
        RecyclerListView recyclerView = mAdapter.getListController().getRecyclerView();
        if (recyclerView.isLoadMoreEnable()) {
            onStateChanged(ILoadFooterCreator.State.LOADING_MORE);
        } else {
            onStateChanged(ILoadFooterCreator.State.NO_MORE);
        }
    }

    public abstract void onStateChanged(ILoadFooterCreator.State state);
}
