package com.github.daweizhou89.reqlist.holder;

import android.databinding.ViewDataBinding;
import android.support.annotation.CallSuper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.daweizhou89.reqlist.adapter.BaseListAdapter;

/**
 * Created by daweizhou89 on 2016/11/14.
 */
public class BaseViewHolder<VDB extends ViewDataBinding> extends RecyclerView.ViewHolder implements View.OnClickListener {

    public VDB binding;

    public BaseListAdapter adapter;

    public int position;

    public boolean attach;

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public BaseViewHolder(VDB binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bindData(int position) {
        // TODO nothing
    }

    @Override
    @CallSuper
    public void onClick(View v) {
        beforeClick(v, position);
        onClick(v, position);
    }

    public void beforeClick(View v, int position) {
        // TODO nothing
    }

    public void onClick(View v, int position) {
        // TODO nothing
    }
}
