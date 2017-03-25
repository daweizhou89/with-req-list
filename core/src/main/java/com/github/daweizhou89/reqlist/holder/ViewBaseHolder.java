package com.github.daweizhou89.reqlist.holder;

import android.databinding.ViewDataBinding;
import android.support.annotation.CallSuper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by daweizhou89 on 2016/11/14.
 */
public class ViewBaseHolder<VDB extends ViewDataBinding> extends RecyclerView.ViewHolder implements View.OnClickListener {

    public VDB binding;

    public ViewBaseHolder(View itemView) {
        super(itemView);
    }

    public ViewBaseHolder(VDB binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bindData(int position) {
        // TODO nothing
    }

    @Override
    @CallSuper
    public void onClick(View v) {
        beforeSharedClick(v);
        onSharedClick(v);
    }

    public void beforeSharedClick(View v) {
        // TODO nothing
    }

    public void onSharedClick(View v) {
        // TODO nothing
    }
}
