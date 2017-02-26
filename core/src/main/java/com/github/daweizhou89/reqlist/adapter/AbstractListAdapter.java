package com.github.daweizhou89.reqlist.adapter;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.v7.widget.RecyclerView;

import com.github.daweizhou89.listview.adapter.AnimationBaseAdapter;
import com.github.daweizhou89.reqlist.holder.AbstractViewHolder;
import com.github.daweizhou89.reqlist.manager.AbstractListManager;
import com.github.daweizhou89.reqlist.model.ListItem;

import java.util.BitSet;


public abstract class AbstractListAdapter<VH extends RecyclerView.ViewHolder> extends AnimationBaseAdapter<VH> {

    protected AbstractListManager mListManager;

    protected BitSet mDataDirty = new BitSet();

    public AbstractListAdapter(Context context, AbstractListManager listManager) {
        super(context);
        mListManager = listManager;
    }

    public void setListManager(AbstractListManager listManager) {
        this.mListManager = listManager;
    }

    public AbstractListManager getListManager() {
        return mListManager;
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
        if (holder instanceof AbstractViewHolder) {
            AbstractViewHolder viewHolder = (AbstractViewHolder) holder;
            viewHolder.bindData(position);
        }
    }

    @Override
    public int getItemCount() {
        return mListManager.getListItems().size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return mListManager.getListItem(position).getType();
    }

    public ListItem getListItem(int position) {
        return mListManager.getListItem(position);
    }

    @CallSuper
    public void setDataDirty() {
        //...
    }

    public boolean hasDataDirty() {
        return !mDataDirty.isEmpty();
    }

    public interface IViewAttachable {

        void onViewAttachedToWindow();

        void onViewDetachedFromWindow();
    }

    public interface INormalLoadMore {

        void onLoadMoreComplete();

        void setLoadingMoreText(String loadingMoreText);

        void setLoadingNoMoreText(String loadingNoMoreText);
    }

}
