package com.github.daweizhou89.reqlist.sample;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.daweizhou89.reqlist.ReqListContext;
import com.github.daweizhou89.reqlist.sample.adapter.ResultListAdapter;
import com.github.daweizhou89.reqlist.sample.controller.RxGossipLocationListController;
import com.github.daweizhou89.reqlist.sample.databinding.ActivityList3Binding;

public class List3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityList3Binding binding = DataBindingUtil.setContentView(this, R.layout.activity_list3);

        ReqListContext reqListContext = new ReqListContext.Builder(this, new ResultListAdapter(this)).build();
        RxGossipLocationListController listController = new RxGossipLocationListController(reqListContext);
        binding.listContentView
                .getInitHelper()
                .setListController(listController)
                .setSwipeRefreshWrapperId(R.id.content_list)
                .setRequestData(false) // not requestData() when init
                .init();

        binding.listContentView.requestData();
    }
}
