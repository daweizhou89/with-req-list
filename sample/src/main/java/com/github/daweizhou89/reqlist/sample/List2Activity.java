package com.github.daweizhou89.reqlist.sample;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.daweizhou89.reqlist.ReqListContext;
import com.github.daweizhou89.reqlist.sample.adapter.ResultListAdapter;
import com.github.daweizhou89.reqlist.sample.controller.GossipLocationListController;
import com.github.daweizhou89.reqlist.sample.databinding.ActivityList2Binding;

public class List2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityList2Binding binding = DataBindingUtil.setContentView(this, R.layout.activity_list2);

        ReqListContext reqListContext = new ReqListContext.Builder(this, new ResultListAdapter(this)).build();
        GossipLocationListController listManager = new GossipLocationListController(reqListContext);
        binding.listContentView
                .getInitHelper()
                .setListController(listManager)
                .setSwipeRefreshWrapperId(R.id.content_list)
                .init();
    }
}
