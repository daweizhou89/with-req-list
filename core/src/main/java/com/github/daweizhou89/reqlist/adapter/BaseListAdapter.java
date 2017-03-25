package com.github.daweizhou89.reqlist.adapter;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.v7.widget.RecyclerView;

import com.github.daweizhou89.listview.RecyclerListView;
import com.github.daweizhou89.listview.adapter.AnimationBaseAdapter;
import com.github.daweizhou89.reqlist.ReqListContext;
import com.github.daweizhou89.reqlist.controler.BaseListController;
import com.github.daweizhou89.reqlist.holder.ViewBaseHolder;
import com.github.daweizhou89.reqlist.interfaces.IViewAttachable;
import com.github.daweizhou89.reqlist.model.ListItem;


public abstract class BaseListAdapter<VH extends RecyclerView.ViewHolder> extends AnimationBaseAdapter<VH> {

    protected BaseListController mListController;

    protected ReqListContext mReqListContext;

    public BaseListAdapter(Context context) {
        super(context);
    }

    public void setListController(BaseListController listController) {
        this.mListController = listController;
        mReqListContext = listController.getContextHolder();
    }

    @Override
    @CallSuper
    public void onViewDetachedFromWindow(VH holder) {
        super.onViewDetachedFromWindow(holder);
        if (holder instanceof IViewAttachable) {
            ((IViewAttachable) holder).onViewDetachedFromWindow();
        }
    }

    @Override
    @CallSuper
    public void onViewAttachedToWindow(VH holder) {
        super.onViewAttachedToWindow(holder);
        if (holder instanceof IViewAttachable) {
            ((IViewAttachable) holder).onViewAttachedToWindow();
        }
    }

    @Override
    @CallSuper
    public void onBindViewHolderII(VH holder, int position) {
        if (holder instanceof ViewBaseHolder) {
            ViewBaseHolder viewHolder = (ViewBaseHolder) holder;
            viewHolder.bindData(position);
        }
    }

    @Override
    public int getItemCount() {
        return mReqListContext.getListItemCount();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return mReqListContext.getListItem(position).getType();
    }

    public ListItem getListItem(int position) {
        return mReqListContext.getListItem(position);
    }

    public final void onLoadMoreComplete() {
        RecyclerListView recyclerView = mListController.getRecyclerView();
        if (this instanceof ILoadFooterCreator && !recyclerView.isLoadMoreEnable()) {
            ((ILoadFooterCreator) this).onLoadFooterStateChanged(ILoadFooterCreator.State.NO_MORE);
        }
    }

}
