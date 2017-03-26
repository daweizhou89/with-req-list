package com.github.daweizhou89.reqlist.sample.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.github.daweizhou89.reqlist.adapter.BaseListAdapter;
import com.github.daweizhou89.reqlist.holder.BaseLoadFooterHolder;
import com.github.daweizhou89.reqlist.adapter.BaseLoadFooterListAdapter;
import com.github.daweizhou89.reqlist.adapter.ILoadFooterCreator;
import com.github.daweizhou89.reqlist.holder.ListBaseViewHolder;
import com.github.daweizhou89.reqlist.model.ListItem;
import com.github.daweizhou89.reqlist.sample.R;
import com.github.daweizhou89.reqlist.sample.holder.LoadFooterHolder;
import com.github.daweizhou89.reqlist.sample.databinding.LoadFooterItemLayoutBinding;
import com.github.daweizhou89.reqlist.sample.databinding.ResultItemLayoutBinding;
import com.github.daweizhou89.reqlist.sample.model.Result;

/**
 *
 * Created by daweizhou89 on 2017/2/23.
 */

public class ResultListAdapter extends BaseLoadFooterListAdapter {

    public ResultListAdapter(Context context) {
        super(context);
    }

    private LoadFooterItemLayoutBinding mLoadFooterBinding;

    @Override
    public void onLoadFooterStateChanged(State state) {
        if (mLoadFooterBinding != null) {
            if (state == ILoadFooterCreator.State.NO_MORE) {
                mLoadFooterBinding.text.setText(getReqListContext().getNoMoreText());
            } else {
                mLoadFooterBinding.text.setText(getReqListContext().getLoadingMoreText());
            }
        }
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolderII(ViewGroup parent, int viewType) {
        ResultItemLayoutBinding binding = DataBindingUtil.inflate(mLayoutInflater, R.layout.result_item_layout, parent, false);
        return new ViewHolder(this, binding);
    }

    @Override
    protected BaseLoadFooterHolder onCreateLoadViewHolder(ViewGroup parent) {
        mLoadFooterBinding = DataBindingUtil.inflate(mLayoutInflater, R.layout.load_footer_item_layout, parent, false);
        return new LoadFooterHolder(this, mLoadFooterBinding);
    }

    public static class ViewHolder extends ListBaseViewHolder<ResultItemLayoutBinding> {

        public ViewHolder(BaseListAdapter adapter, ResultItemLayoutBinding binding) {
            super(adapter, binding);
        }

        @Override
        public void bindData(int position) {
            ListItem listItem = mAdapter.getListItem(position);
            Result data = (Result) listItem.getData();
            binding.text.setText(listItem.getIndexOfType() + ". " + data.key);
        }
    }

}
