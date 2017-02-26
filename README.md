# with-req-list
with-req-list基于RecyclerView，SwipeRefreshLayout封装，目的是简化列表数据请求和实现。

## xml配置
xml中添加, 例如sample中的activity_list1.xml
```xml
<com.github.daweizhou89.reqlist.view.ListContentView
    android:id="@+id/list_content_view"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <com.github.daweizhou89.listview.SwipeRefreshListLayout
        android:id="@+id/content_list"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>

    <com.github.daweizhou89.reqlist.sample.view.LoadTipsView
        android:id="@+id/load_tips_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="@android:color/white" />

</com.github.daweizhou89.reqlist.view.ListContentView>
```

## CommonListManager例子
参考sample中List1Activity
```java
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
```

ResultAdapter
```java
public class ResultAdapter extends AbstractListAdapter {

    public ResultAdapter(Context context, AbstractListManager listManager) {
        super(context, listManager);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ResultItemLayoutBinding binding = DataBindingUtil.inflate(mLayoutInflater, R.layout.result_item_layout, parent, false);
        return new ViewHolder(this, binding);
    }

    public static class ViewHolder extends AbstractListViewHolder<ResultItemLayoutBinding> {

        public ViewHolder(AbstractListAdapter adapter, ResultItemLayoutBinding binding) {
            super(adapter, binding);
        }

        @Override
        public void bindData(int position) {
            ListItem listItem = mAdapter.getListItem(position);
            Result data = (Result) listItem.getData();
            binding.text.setText(listItem.getIndexOfType() + ". " + data.key);
        }
    }
```

## 自定义ListManager例子
参考sample中List2Activity
```java
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
```

GossipLocationManager
```java
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
```

GossipLocationRequester
```java
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
```
