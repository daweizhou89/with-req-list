package com.github.daweizhou89.reqlist.sample.manager;

import android.content.Context;

import com.github.daweizhou89.reqlist.adapter.AbstractListAdapter;
import com.github.daweizhou89.reqlist.manager.AbstractListManager;
import com.github.daweizhou89.reqlist.sample.adapter.ResultAdapter;
import com.github.daweizhou89.reqlist.sample.requester.GossipLocationRequester;

/**
 *
 * Created by daweizhou89 on 2017/2/26.
 */

public class GossipLocationManager extends AbstractListManager {

    private GossipLocationRequester mGossipLocationRequester;

    public GossipLocationManager(Context context) {
        super(context);
        mGossipLocationRequester = new GossipLocationRequester(this);
    }

    @Override
    protected AbstractListAdapter onCreateListAdapter() {
        return new ResultAdapter(mActivity, this);
    }

    @Override
    protected void onBuildList() {
        mGossipLocationRequester.build();
    }

    @Override
    protected void onRequestData() {
        mGossipLocationRequester.request();
    }

    @Override
    protected void onRequestMoreAppend() {
        // TODO nothing
    }
}
