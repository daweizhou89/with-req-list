package com.github.daweizhou89.reqlist.sample;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.daweizhou89.okhttpclientutils.OkHttpClientUtils;
import com.github.daweizhou89.reqlist.DebugLog;
import com.github.daweizhou89.reqlist.manager.CommonListManager;
import com.github.daweizhou89.reqlist.requester.AbstractRequester;
import com.github.daweizhou89.reqlist.requester.CommonRequester;
import com.github.daweizhou89.reqlist.sample.adapter.ResultAdapter;
import com.github.daweizhou89.reqlist.sample.databinding.ActivityList1Binding;
import com.github.daweizhou89.reqlist.sample.model.Gossip;
import com.github.daweizhou89.reqlist.sample.model.Response;
import com.github.daweizhou89.reqlist.sample.model.Result;
import com.google.gson.Gson;

import java.util.List;

public class List1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityList1Binding binding = DataBindingUtil.setContentView(this, R.layout.activity_list1);
        CommonListManager listManager = new CommonListManager.Builder(this)
                .setUrl("http://sugg.us.search.yahoo.net/gossip-gl-location/?appid=weather&output=json&command=%E5%B9%BF")
                .setItemParser(new CommonRequester.ItemParser() {
                    @Override
                    public List parseItems(String response) {
                        return null;
                    }
                })
                .setLoadMoreEnable(false)
                .setItemTag("list1")
                .setItemType(ItemType.TYPE_RESULT)
                .setOnRequestImpl(new CommonRequester.OnRequestImpl() {
                    @Override
                    public void onRequest(int pageNo, AbstractRequester.CallBack callback, Object... inputs) {
                        OkHttpClientUtils.get(requester.getUrl(), null, callback);
                    }

                    @Override
                    public void onRequestMore(int morePageNo, AbstractRequester.MoreCallback callback, Object... inputs) {
                        // TODO nothing
                    }
                })
                .setItemParser(new CommonRequester.ItemParser() {
                    @Override
                    public List parseItems(String responseStr) {
                        Response response = null;
                        try {
                            response = new Gson().fromJson(responseStr, Response.class);
                        } catch (Exception e) {
                            DebugLog.e(e);
                        }
                        if (response == null) {
                            return null;
                        }
                        Gossip gossip = response.gossip;
                        List<Result> results = gossip != null ? gossip.results : null;
                        return results;
                    }
                })
                .build();

        ResultAdapter adapter = new ResultAdapter(this, listManager);
        listManager.setAdapter(adapter);

        binding.listContentView
                .getInitHelper()
                .setListManager(listManager)
                .setSwipeRefreshListViewId(R.id.content_list)
                .setLoadTipsId(R.id.load_tips_view)
                .init();
    }
}
