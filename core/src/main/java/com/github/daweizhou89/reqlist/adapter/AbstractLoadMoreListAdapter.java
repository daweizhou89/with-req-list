package com.github.daweizhou89.reqlist.adapter;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.CallSuper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.github.daweizhou89.listview.RecyclerListView;
import com.github.daweizhou89.reqlist.holder.AbstractViewHolder;
import com.github.daweizhou89.reqlist.manager.AbstractListManager;
import com.github.daweizhou89.reqlist.model.ListItem;

/**
 * Created by daweizhou89 on 16/10/26.
 */
public abstract class AbstractLoadMoreListAdapter extends AbstractListAdapter implements AbstractListAdapter.INormalLoadMore {

    private static final long DELAY_REQUEST_MORE = 800;

    private static final int MSG_REQUEST_MORE = 100;

    public static final int STATE_LOADING_MORE = 1;

    public static final int STATE_LOADING_NO_MORE = 2;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REQUEST_MORE:
                    RecyclerListView recyclerView = mListManager.getRecyclerView();
                    recyclerView.performLoadMore();
                    if (recyclerView.isLoadingMore()) {
                        onStateChanged(State.LOADING_MORE);
                    } else {
                        onStateChanged(State.NO_MORE);
                    }
                    break;
            }
        }
    };

    public AbstractLoadMoreListAdapter(Context context, AbstractListManager listManager) {
        super(context, listManager);
    }

    @Override
    @CallSuper
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ListItem.TYPE_LOAD_MORE:
                return onCreateLoadViewHolder(parent);
        }
        return onCreateViewHolderII(parent, viewType);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        mHandler.removeMessages(MSG_REQUEST_MORE);
        super.onDetachedFromRecyclerView(recyclerView);
    }

    public void onLoadMoreComplete() {
        RecyclerListView recyclerView = mListManager.getRecyclerView();
        if (!recyclerView.isLoadMoreEnable()) {
            onStateChanged(State.NO_MORE);
        }
    }

    @Override
    public void setDataDirty() {
        super.setDataDirty();
        mDataDirty.set(ListItem.TYPE_LOAD_MORE);
    }

    private void requestMoreDelay() {
        mHandler.sendEmptyMessageDelayed(MSG_REQUEST_MORE, DELAY_REQUEST_MORE);
    }

    protected abstract RecyclerView.ViewHolder onCreateViewHolderII(ViewGroup parent, int viewType);

    protected abstract AbstractLoadViewHolder onCreateLoadViewHolder(ViewGroup parent);

    protected abstract void onStateChanged(State state);

    protected enum State {
        LOADING_MORE,
        NO_MORE
    }

    public abstract class AbstractLoadViewHolder<VDB extends ViewDataBinding> extends AbstractViewHolder<VDB> implements IViewAttachable {

        public AbstractLoadViewHolder(View itemView) {
            super(itemView);
        }

        public AbstractLoadViewHolder(VDB binding) {
            super(binding);
        }

        @Override
        @CallSuper
        public void onViewAttachedToWindow() {
            RecyclerListView recyclerView = mListManager.getRecyclerView();
            onStateChanged(State.LOADING_MORE);
            if (recyclerView.isNormalLoadMoreMode()
                    && recyclerView.isLoadMoreEnable()
                    && !recyclerView.isLoadingMore()
                    && !mHandler.hasMessages(MSG_REQUEST_MORE)) {
                requestMoreDelay();
            } else if (recyclerView.isNormalLoadMoreMode() && !recyclerView.isLoadMoreEnable()) {
                onStateChanged(State.NO_MORE);
            }
        }

        protected abstract void onStateChanged(State state);
    }
}
