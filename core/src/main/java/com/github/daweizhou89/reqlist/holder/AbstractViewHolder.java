package com.github.daweizhou89.reqlist.holder;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by daweizhou89 on 2016/11/14.
 */
public class AbstractViewHolder<VDB extends ViewDataBinding> extends RecyclerView.ViewHolder {

    public VDB binding;

    public AbstractViewHolder(View itemView) {
        super(itemView);
    }

    public AbstractViewHolder(VDB binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bindData(int position) {
        // TODO nothing
    }
}
