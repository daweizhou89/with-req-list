package com.github.daweizhou89.reqlist.adapter;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.github.daweizhou89.listview.RecyclerListView;
import com.github.daweizhou89.listview.adapter.BaseAnimationAdapter;
import com.github.daweizhou89.reqlist.ReqListContext;
import com.github.daweizhou89.reqlist.controler.BaseListController;
import com.github.daweizhou89.reqlist.holder.BaseViewHolder;
import com.github.daweizhou89.reqlist.interfaces.IViewAttachable;
import com.github.daweizhou89.reqlist.model.ListItem;


public abstract class BaseListAdapter<VH extends RecyclerView.ViewHolder> extends BaseAnimationAdapter<VH> {

    protected BaseListController mListController;

    protected ReqListContext mReqListContext;

    public BaseListAdapter(Context context) {
        super(context);
    }

    public final void setListController(BaseListController listController) {
        this.mListController = listController;
        mReqListContext = listController.getContextHolder();
    }

    public BaseListController getListController() {
        return mListController;
    }

    public ReqListContext getReqListContext() {
        return mReqListContext;
    }

    @Override
    @CallSuper
    public void onViewDetachedFromWindow(VH holder) {
        super.onViewDetachedFromWindow(holder);
        if (holder instanceof BaseViewHolder) {
            ((BaseViewHolder) holder).attach = false;
        }
        if (holder instanceof IViewAttachable) {
            ((IViewAttachable) holder).onViewDetachedFromWindow();
        }
    }

    @Override
    @CallSuper
    public void onViewAttachedToWindow(VH holder) {
        super.onViewAttachedToWindow(holder);
        if (holder instanceof BaseViewHolder) {
            ((BaseViewHolder) holder).attach = true;
        }
        if (holder instanceof IViewAttachable) {
            ((IViewAttachable) holder).onViewAttachedToWindow();
        }
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return onCreateViewHolderII(parent, viewType);
    }

    @Override
    @CallSuper
    public void onBindViewHolderII(VH holder, int position) {
        if (holder instanceof BaseViewHolder) {
            BaseViewHolder viewHolder = (BaseViewHolder) holder;
            viewHolder.position = position;
            viewHolder.adapter = this;
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

    protected abstract VH onCreateViewHolderII(ViewGroup parent, int viewType);

}
