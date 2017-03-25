package com.github.daweizhou89.reqlist.adapter;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.annotation.CallSuper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.github.daweizhou89.listview.RecyclerListView;
import com.github.daweizhou89.reqlist.holder.ViewBaseHolder;
import com.github.daweizhou89.reqlist.interfaces.IViewAttachable;
import com.github.daweizhou89.reqlist.model.ListItem;

/**
 * Created by daweizhou89 on 16/10/26.
 */
public abstract class LoadFooterBaseListAdapter extends BaseListAdapter implements ILoadFooterCreator {

    public LoadFooterBaseListAdapter(Context context) {
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

    protected abstract RecyclerView.ViewHolder onCreateViewHolderII(ViewGroup parent, int viewType);

    protected abstract LoadFooterBaseHolder onCreateLoadViewHolder(ViewGroup parent);

    public abstract class LoadFooterBaseHolder<VDB extends ViewDataBinding> extends ViewBaseHolder<VDB> implements IViewAttachable {

        public LoadFooterBaseHolder(View itemView) {
            super(itemView);
        }

        public LoadFooterBaseHolder(VDB binding) {
            super(binding);
        }

        @Override
        public final void bindData(int position) {
            RecyclerListView recyclerView = mListController.getRecyclerView();
            if (recyclerView.isLoadMoreEnable()) {
                onStateChanged(State.LOADING_MORE);
            } else {
                onStateChanged(State.NO_MORE);
            }
        }

        protected abstract void onStateChanged(State state);
    }
}
