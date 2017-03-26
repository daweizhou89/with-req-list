package com.github.daweizhou89.reqlist.sample.holder;

import com.github.daweizhou89.reqlist.adapter.BaseListAdapter;
import com.github.daweizhou89.reqlist.holder.BaseLoadFooterHolder;
import com.github.daweizhou89.reqlist.adapter.ILoadFooterCreator;
import com.github.daweizhou89.reqlist.sample.databinding.LoadFooterItemLayoutBinding;

/**
 * Created by zhoudawei on 2017/3/26.
 */
public class LoadFooterHolder extends BaseLoadFooterHolder<LoadFooterItemLayoutBinding> {

    public LoadFooterHolder(BaseListAdapter adapter, LoadFooterItemLayoutBinding binding) {
        super(adapter, binding);
    }

    @Override
    public void onStateChanged(ILoadFooterCreator.State state) {
        if (state == ILoadFooterCreator.State.NO_MORE) {
            binding.text.setText(mAdapter.getReqListContext().getNoMoreText());
        } else {
            binding.text.setText(mAdapter.getReqListContext().getLoadingMoreText());
        }
    }
}
