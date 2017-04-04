package com.github.daweizhou89.reqlist.holder;

import android.databinding.ViewDataBinding;
import android.view.View;

import com.github.daweizhou89.listview.RecyclerListView;
import com.github.daweizhou89.reqlist.adapter.BaseListAdapter;
import com.github.daweizhou89.reqlist.adapter.ILoadFooterCreator;

/**
 * Created by zhoudawei on 2017/3/26.
 */
public abstract class BaseLoadFooterHolder<VDB extends ViewDataBinding> extends BaseViewHolder<VDB> {

    public BaseLoadFooterHolder(View itemView) {
        super(itemView);
    }

    public BaseLoadFooterHolder(VDB binding) {
        super(binding);
    }

    @Override
    public final void bindData(int position) {
        RecyclerListView recyclerView = adapter.getListController().getRecyclerView();
        if (recyclerView.isLoadMoreEnable()) {
            onStateChanged(ILoadFooterCreator.State.LOADING_MORE);
        } else {
            onStateChanged(ILoadFooterCreator.State.NO_MORE);
        }
    }

    public abstract void onStateChanged(ILoadFooterCreator.State state);
}
