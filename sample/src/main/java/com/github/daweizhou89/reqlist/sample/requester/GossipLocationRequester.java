package com.github.daweizhou89.reqlist.sample.requester;

import com.github.daweizhou89.okhttpclientutils.OkHttpClientUtils;
import com.github.daweizhou89.reqlist.DebugLog;
import com.github.daweizhou89.reqlist.manager.AbstractListManager;
import com.github.daweizhou89.reqlist.model.ListItem;
import com.github.daweizhou89.reqlist.requester.AbstractRequester;
import com.github.daweizhou89.reqlist.sample.ItemType;
import com.github.daweizhou89.reqlist.sample.model.Gossip;
import com.github.daweizhou89.reqlist.sample.model.Response;
import com.github.daweizhou89.reqlist.sample.model.Result;
import com.google.gson.Gson;

import java.util.List;

/**
 *
 * Created by daweizhou89 on 2017/2/26.
 */

public class GossipLocationRequester extends AbstractRequester<Result> {

    public GossipLocationRequester(AbstractListManager listManager) {
        super(listManager);
    }

    @Override
    protected void onResponse(String url, String responseStr) {
        Response response = null;
        try {
            response = new Gson().fromJson(responseStr, Response.class);
        } catch (Exception e) {
            DebugLog.e(e);
        }
        Gossip gossip = response.gossip;
        List<Result> results = gossip != null ? gossip.results : null;
        setData(results);
    }

    @Override
    protected void onResponseMore(String url, String response) {
        // TODO nothing
    }

    @Override
    protected void onRequest(int pageNo, CallBack callback, Object... inputs) {
        OkHttpClientUtils.get("http://sugg.us.search.yahoo.net/gossip-gl-location/?appid=weather&output=json&command=%E5%B9%BF", null, callback);
    }

    @Override
    protected void onRequestMore(int morePageNo, MoreCallback callback, Object... inputs) {
        // TODO nothing
    }

    @Override
    protected void onBuild(Object... inputs) {
        if (isEmpty()) {
            return;
        }
        for (Result data : mData) {
            addListItem(new ListItem(ItemType.TYPE_RESULT, data));
        }
    }
}
