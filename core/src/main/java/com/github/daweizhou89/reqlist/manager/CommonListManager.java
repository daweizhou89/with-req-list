package com.github.daweizhou89.reqlist.manager;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.github.daweizhou89.reqlist.adapter.AbstractListAdapter;
import com.github.daweizhou89.reqlist.requester.CommonRequester;
import com.github.daweizhou89.reqlist.view.ILoadTips;

import java.util.List;

public class CommonListManager<AT extends Activity> extends AbstractListManager<AT> {

    protected int mItemType;

    protected String mItemTag;

    protected AbstractListAdapter mAdapter;

    protected CommonRequester mRequester;

    private CommonListManager(Context context) {
        super(context);
    }

    public void setAdapter(AbstractListAdapter adapter) {
        this.mAdapter = adapter;
    }

    public CommonRequester getRequester() {
        return mRequester;
    }

    @Override
    protected void onBuildList() {
        mRequester.build();
    }

    protected List getData() {
        return mRequester.getData();
    }

    @Override
    protected  void onRequestData() {
        mRequester.request();
    }

    @Override
    protected void onRequestMoreAppend() {
        mRequester.requestMore();
    }

    @Override
    public boolean isEmpty() {
        return mRequester.isEmpty();
    }

    @Override
    protected AbstractListAdapter onCreateListAdapter() {
        return mAdapter;
    }

    /***
     * 数据类型，adapter创建view使用
     * @return
     */
    public int getItemType() {
        return mItemType;
    }

    /***
     * 类型tag，复用itemView时区分来源使用（统计需要）
     * @return
     */
    public String getItemTag() {
        return mItemTag;
    }

    public static class Builder {

        Context context;

        String url;

        boolean needLoginInfo;

        int itemType;

        String itemTag;

        String cacheKey;

        boolean loadMoreEnable = true;

        int tipsLoadedEmpty = ILoadTips.TIPS_NULL;

        int pageSize;

        CommonRequester.OnBuildListImpl onBuildListImpl;

        CommonRequester.ItemParser itemParser;

        CommonRequester.OnRequestImpl onRequestImpl;

        ListManagerViewHolder listManagerViewHolder;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setPageSize(int pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public Builder setNeedLoginInfo(boolean needLoginInfo) {
            this.needLoginInfo = needLoginInfo;
            return this;
        }

        public Builder setLoadMoreEnable(boolean loadMoreEnable) {
            this.loadMoreEnable = loadMoreEnable;
            return this;
        }

        public Builder setItemType(int itemType) {
            this.itemType = itemType;
            return this;
        }

        public Builder setItemTag(String itemTag) {
            this.itemTag = itemTag;
            return this;
        }

        public Builder setCacheKey(String cacheKey) {
            this.cacheKey = cacheKey;
            return this;
        }

        public Builder setOnBuildListImpl(CommonRequester.OnBuildListImpl onBuildListImpl) {
            this.onBuildListImpl = onBuildListImpl;
            return this;
        }

        public Builder setItemParser(CommonRequester.ItemParser itemParser) {
            this.itemParser = itemParser;
            return this;
        }

        public Builder setOnRequestImpl(CommonRequester.OnRequestImpl onRequestImpl) {
            this.onRequestImpl = onRequestImpl;
            return this;
        }

        /***
         * 设定一个新空类型代替 {@link com.github.daweizhou89.reqlist.view.ILoadTips#TIPS_LOADED_EMPTY}
         * 用来不同的列表为空提示样式
         * @param tipsLoadedEmpty
         * @return
         */
        public Builder setTipsLoadedEmpty(int tipsLoadedEmpty) {
            this.tipsLoadedEmpty = tipsLoadedEmpty;
            return this;
        }

        public Builder setListManagerViewHolder(ListManagerViewHolder listManagerViewHolder) {
            this.listManagerViewHolder = listManagerViewHolder;
            return this;
        }

        public CommonListManager build() {
            CommonListManager listManager = new CommonListManager(context);

            listManager.mItemType = itemType;
            listManager.mItemTag = itemTag;
            if (tipsLoadedEmpty != ILoadTips.TIPS_NULL) {
                listManager.setTipsLoadedEmpty(tipsLoadedEmpty);
            }
            if (listManagerViewHolder != null) {
                listManager.setListManagerViewHolder(listManagerViewHolder);
            }
            CommonRequester.Builder requesterBuilder = new CommonRequester.Builder(listManager)
                    .setUrl(url)
                    .setCacheKey(cacheKey)
                    .setNeedLoginInfo(needLoginInfo)
                    .setLoadMoreEnable(loadMoreEnable)
                    .setItemParser(itemParser)
                    .setOnRequestImpl(onRequestImpl)
                    .setOnBuildListImpl(onBuildListImpl);
            if (pageSize > 0) {
                requesterBuilder.setPageSize(pageSize);
            }
            CommonRequester requester = requesterBuilder.build();
            listManager.mRequester = requester;

            return listManager;
        }
    }
}
