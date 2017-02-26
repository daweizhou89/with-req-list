package com.github.daweizhou89.reqlist.sample.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.github.daweizhou89.reqlist.adapter.AbstractListAdapter;
import com.github.daweizhou89.reqlist.holder.AbstractListViewHolder;
import com.github.daweizhou89.reqlist.manager.AbstractListManager;
import com.github.daweizhou89.reqlist.model.ListItem;
import com.github.daweizhou89.reqlist.sample.R;
import com.github.daweizhou89.reqlist.sample.databinding.ResultItemLayoutBinding;
import com.github.daweizhou89.reqlist.sample.model.Result;

/**
 *
 * Created by daweizhou89 on 2017/2/23.
 */

public class ResultAdapter extends AbstractListAdapter {

    public ResultAdapter(Context context, AbstractListManager listManager) {
        super(context, listManager);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ResultItemLayoutBinding binding = DataBindingUtil.inflate(mLayoutInflater, R.layout.result_item_layout, parent, false);
        return new ViewHolder(this, binding);
    }

    public static class ViewHolder extends AbstractListViewHolder<ResultItemLayoutBinding> {

        public ViewHolder(AbstractListAdapter adapter, ResultItemLayoutBinding binding) {
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
