package com.github.daweizhou89.reqlist.sample.loader;

import com.github.daweizhou89.okhttpclientutils.OkHttpClientUtils;
import com.github.daweizhou89.reqlist.controler.BaseListController;
import com.github.daweizhou89.reqlist.loader.BaseRxLoader;
import com.github.daweizhou89.reqlist.model.ListItem;
import com.github.daweizhou89.reqlist.sample.ItemType;
import com.github.daweizhou89.reqlist.sample.model.Gossip;
import com.github.daweizhou89.reqlist.sample.model.Response;
import com.github.daweizhou89.reqlist.sample.model.Result;

import java.util.List;

import io.reactivex.Observable;

/**
 *
 * Created by daweizhou89 on 2017/2/26.
 */

public class RxGossipLocationLoader extends BaseRxLoader<Result, Response> {

    public RxGossipLocationLoader(BaseListController listController) {
        super(listController, true);
    }

    @Override
    public void onResponse(Response response, boolean more) {
        Gossip gossip = response.gossip;
        List<Result> results = gossip != null ? gossip.results : null;
        setData(results);
    }

    @Override
    public Observable<Response> onCreateObservable(int pageNo, boolean more, Object... inputs) {
        return OkHttpClientUtils.get("http://sugg.us.search.yahoo.net/gossip-gl-location/?appid=weather&output=json&command=%E5%B9%BF", null, Response.class);
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
