package com.github.daweizhou89.reqlist.sample.loader;

import com.github.daweizhou89.okhttpclientutils.OkHttpClientUtils;
import com.github.daweizhou89.reqlist.DebugLog;
import com.github.daweizhou89.reqlist.controler.BaseListController;
import com.github.daweizhou89.reqlist.loader.http.BaseHttpLoader;
import com.github.daweizhou89.reqlist.loader.http.ResponseCallBack;
import com.github.daweizhou89.reqlist.model.ListItem;
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

public class GossipLocationLoader extends BaseHttpLoader<Result> {

    public GossipLocationLoader(BaseListController listController) {
        super(listController, true);
    }

    @Override
    public void onResponse(String url, String responseStr, boolean more) {
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
    protected void onLoad(int pageNo, boolean more, ResponseCallBack callback, Object... inputs) {
        OkHttpClientUtils.get("http://sugg.us.search.yahoo.net/gossip-gl-location/?appid=weather&output=json&command=%E5%B9%BF", null, callback);
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
