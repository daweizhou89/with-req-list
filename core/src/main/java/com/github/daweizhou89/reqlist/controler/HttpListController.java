package com.github.daweizhou89.reqlist.controler;

import com.github.daweizhou89.reqlist.ReqListContext;
import com.github.daweizhou89.reqlist.loader.http.CommonHttpLoader;

import java.util.List;

public class HttpListController extends BaseListController {

    protected int mItemType;

    protected String mItemTag;

    protected CommonHttpLoader mLoader;

    private HttpListController(ReqListContext reqListContext) {
        super(reqListContext);
    }

    protected List getData() {
        return mLoader.getData();
    }

    @Override
    protected void onBuildList() {
        mLoader.build();
    }

    @Override
    protected  void onRequestData(boolean more) {
        mLoader.load(more);
    }

    @Override
    public boolean isEmpty() {
        return mLoader.isEmpty();
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

        ReqListContext reqListContext;

        String url;

        boolean needLoginInfo;

        int itemType;

        String itemTag;

        String cacheKey;

        boolean loadMore = true;

        int pageSize;

        CommonHttpLoader.OnBuildListImpl onBuildListImpl;

        CommonHttpLoader.ItemParser itemParser;

        CommonHttpLoader.OnLoadImpl onLoadImpl;

        public Builder(ReqListContext reqListContext) {
            this.reqListContext = reqListContext;
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

        public Builder setLoadMore(boolean loadMore) {
            this.loadMore = loadMore;
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

        public Builder setOnBuildListImpl(CommonHttpLoader.OnBuildListImpl onBuildListImpl) {
            this.onBuildListImpl = onBuildListImpl;
            return this;
        }

        public Builder setItemParser(CommonHttpLoader.ItemParser itemParser) {
            this.itemParser = itemParser;
            return this;
        }

        public Builder setOnLoadImpl(CommonHttpLoader.OnLoadImpl onLoadImpl) {
            this.onLoadImpl = onLoadImpl;
            return this;
        }

        public HttpListController build() {
            HttpListController listManager = new HttpListController(reqListContext);
            listManager.mItemType = itemType;
            listManager.mItemTag = itemTag;
            CommonHttpLoader.Builder requesterBuilder = new CommonHttpLoader.Builder(listManager)
                    .setUrl(url)
                    .setCacheKey(cacheKey)
                    .setNeedLoginInfo(needLoginInfo)
                    .setLoadMore(loadMore)
                    .setItemParser(itemParser)
                    .setOnLoadImpl(onLoadImpl)
                    .setOnBuildListImpl(onBuildListImpl);
            if (pageSize > 0) {
                requesterBuilder.setPageSize(pageSize);
            }
            CommonHttpLoader requester = requesterBuilder.build();
            listManager.mLoader = requester;

            return listManager;
        }
    }
}
