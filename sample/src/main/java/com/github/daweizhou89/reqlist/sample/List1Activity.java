package com.github.daweizhou89.reqlist.sample;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.daweizhou89.okhttpclientutils.OkHttpClientUtils;
import com.github.daweizhou89.reqlist.DebugLog;
import com.github.daweizhou89.reqlist.ReqListContext;
import com.github.daweizhou89.reqlist.controler.HttpListController;
import com.github.daweizhou89.reqlist.loader.http.CommonHttpLoader;
import com.github.daweizhou89.reqlist.loader.http.ResponseCallBack;
import com.github.daweizhou89.reqlist.sample.adapter.ResultListAdapter;
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
        ReqListContext reqListContext = new ReqListContext.Builder(this, new ResultListAdapter(this)).build();
        HttpListController listController = new HttpListController.Builder(reqListContext)
                .setUrl("http://sugg.us.search.yahoo.net/gossip-gl-location/?appid=weather&output=json&command=%E5%B9%BF")
                .setItemParser(new CommonHttpLoader.ItemParser() {
                    @Override
                    public List parseItems(String response) {
                        return null;
                    }
                })
                .setLoadMore(false)
                .setItemTag("list1")
                .setItemType(ItemType.TYPE_RESULT)
                .setOnLoadImpl(new CommonHttpLoader.OnLoadImpl() {
                    @Override
                    public void onLoad(int pageNo, boolean more, ResponseCallBack callback, Object... inputs) {
                        OkHttpClientUtils.get(loader.getUrl(), null, callback);
                    }
                })
                .setItemParser(new CommonHttpLoader.ItemParser() {
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

        binding.listContentView
                .getInitHelper()
                .setListManager(listController)
                .setSwipeRefreshListId(R.id.content_list)
                .setLoadViewId(R.id.load_tips_view)
                .init();
    }
}
