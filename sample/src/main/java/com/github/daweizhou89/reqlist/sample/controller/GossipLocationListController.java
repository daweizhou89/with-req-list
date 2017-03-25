package com.github.daweizhou89.reqlist.sample.controller;

import com.github.daweizhou89.reqlist.ReqListContext;
import com.github.daweizhou89.reqlist.controler.BaseListController;
import com.github.daweizhou89.reqlist.sample.loader.GossipLocationLoader;

/**
 *
 * Created by daweizhou89 on 2017/2/26.
 */

public class GossipLocationListController extends BaseListController {

    private GossipLocationLoader mGossipLocationRequester;

    public GossipLocationListController(ReqListContext context) {
        super(context);
        mGossipLocationRequester = new GossipLocationLoader(this);
    }

    @Override
    protected void onBuildList() {
        mGossipLocationRequester.build();
    }

    @Override
    protected void onRequestData(boolean more) {
        if (!more) {
            mGossipLocationRequester.load();
        } else {
            // TODO: it runs if loadMore of Loader is true;
        }
    }
}
