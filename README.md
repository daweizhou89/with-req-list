# with-req-list
with-req-list基于RecyclerView，SwipeRefreshLayout封装，目的是简化列表数据请求和实现。

## xml配置
xml中添加, 例如sample中的activity_list1.xml
```xml
<com.github.daweizhou89.reqlist.view.ListContentView
    android:id="@+id/list_content_view"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <com.github.daweizhou89.listview.SwipeRefreshList
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
```

ResultAdapter
```java
public class ResultListAdapter extends BaseListAdapter {

    public ResultListAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ResultItemLayoutBinding binding = DataBindingUtil.inflate(mLayoutInflater, R.layout.result_item_layout, parent, false);
        return new ViewHolder(this, binding);
    }

    public static class ViewHolder extends ListBaseViewHolder<ResultItemLayoutBinding> {

        public ViewHolder(BaseListAdapter adapter, ResultItemLayoutBinding binding) {
            super(adapter, binding);
        }

        @Override
        public void bindData(int position) {
            ListItem listItem = mAdapter.getListItem(position);
            Result data = (Result) listItem.getData();
            binding.text.setText(listItem.getIndexOfType() + ". " + data.key);
        }
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

        ReqListContext reqListContext = new ReqListContext.Builder(this, new ResultListAdapter(this)).build();
        GossipLocationListController listManager = new GossipLocationListController(reqListContext);
        binding.listContentView
                .getInitHelper()
                .setListManager(listManager)
                .setSwipeRefreshListId(R.id.content_list)
                .init();
    }
}
```

GossipLocationListController
```java
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
```

GossipLocationLoader
```java
public class GossipLocationLoader extends BaseHttpLoader<Result> {

    public GossipLocationLoader(BaseListController listController) {
        super(listController);
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
```
