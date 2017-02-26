package com.github.daweizhou89.reqlist.sample;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.daweizhou89.reqlist.sample.databinding.ActivityList2Binding;
import com.github.daweizhou89.reqlist.sample.manager.GossipLocationManager;

public class List2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityList2Binding binding = DataBindingUtil.setContentView(this, R.layout.activity_list2);

        GossipLocationManager listManager = new GossipLocationManager(this);
        binding.listContentView
                .getInitHelper()
                .setListManager(listManager)
                .setSwipeRefreshListViewId(R.id.content_list)
                .init();
    }
}
