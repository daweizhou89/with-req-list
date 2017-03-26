package com.github.daweizhou89.reqlist.sample.controller;

import com.github.daweizhou89.reqlist.ReqListContext;
import com.github.daweizhou89.reqlist.controler.BaseListController;
import com.github.daweizhou89.reqlist.sample.loader.RxGossipLocationLoader;

/**
 *
 * Created by daweizhou89 on 2017/2/26.
 */

public class RxGossipLocationListController extends BaseListController {

    private RxGossipLocationLoader mGossipLocationRequester;

    public RxGossipLocationListController(ReqListContext context) {
        super(context);
        mGossipLocationRequester = new RxGossipLocationLoader(this);
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
