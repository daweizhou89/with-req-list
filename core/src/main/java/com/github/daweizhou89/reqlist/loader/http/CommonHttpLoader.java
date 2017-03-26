package com.github.daweizhou89.reqlist.loader.http;

import android.support.annotation.NonNull;

import com.github.daweizhou89.reqlist.DebugLog;
import com.github.daweizhou89.reqlist.controler.BaseListController;
import com.github.daweizhou89.reqlist.controler.HttpListController;
import com.github.daweizhou89.reqlist.model.ListItem;

import java.util.List;

/**
 * Created by daweizhou89 on 16/6/29.
 */
public final class CommonHttpLoader extends BaseHttpLoader {

    protected String mUrl;

    protected boolean mNeedLoginInfo;

    protected String mItemTag;

    protected int mItemType;

    protected String mCacheKey;

    protected OnBuildListImpl mOnBuildListImpl;

    protected OnLoadImpl mOnLoadImpl;

    protected ItemParser mItemParser;

    public CommonHttpLoader(@NonNull BaseListController listManager) {
        super(listManager, true);
    }

    public String getUrl() {
        return mUrl;
    }

    public boolean isNeedLoginInfo() {
        return mNeedLoginInfo;
    }

    public int getItemType() {
        return mItemType;
    }

    public String getItemTag() {
        return mItemTag;
    }

    public List getData() {
        return mData;
    }

    @Override
    public String getCacheKey() {
        return mCacheKey;
    }

    @Override
    protected void onLoad(int pageNo, boolean more, ResponseCallBack callback, Object... inputs) {
        mOnLoadImpl.onLoad(pageNo, more, callback, inputs);
    }

    @Override
    public void onResponse(String response, boolean more) {
        if (mItemParser != null) {
            List items = mItemParser.parseItems(response);
            if (more) {
                appendData(items);
            } else {
                setData(items);
            }
        }
    }

    @Override
    protected void onBuild(Object... inputs) {
        mOnBuildListImpl.onBuild(inputs);
    }

    @Override
    public void appendData(List data) {
        super.appendData(data);
        if (data == null || data.isEmpty()) {
            return;
        }
        int startIndex = mData.size();
        int length = 0;
        for (int i = 0; i < data.size(); i++) {
            Object item = data.get(i);
            if (!mData.contains(item)) {
                mData.add(item);
                ++length;
            }
        }
        if (DebugLog.DEBUG) {
            DebugLog.d(getClass(), "appendData", startIndex + ", " + length);
        }
    }

    public static abstract class OnBuildListImpl {

        protected CommonHttpLoader loader;

        public abstract void onBuild(Object... inputs);

        public CommonHttpLoader getLoader() {
            return loader;
        }

    }

    public static abstract class OnLoadImpl {

        protected CommonHttpLoader loader;

        public abstract void onLoad(int pageNo, boolean more, ResponseCallBack callBack, Object... inputs);

        public CommonHttpLoader getLoader() {
            return loader;
        }
    }

    public static abstract class ItemParser {
        public abstract List parseItems(String response);
    }

    public static class DefaultOnBuildListImpl extends OnBuildListImpl {

        @Override
        public void onBuild(Object... inputs) {
            List data = loader.getData();
            buildData(data, 0, data == null ? 0 : data.size());
        }

        private void buildData(List datas, int startIndex, int length) {
            if (datas == null || datas.isEmpty()) {
                return;
            }
            for (int i = startIndex; i < startIndex + length; i++) {
                buildDataInternal(datas.get(i), i);
            }
        }

        private void buildDataInternal(Object data, int index) {
            final ListItem listItem = new ListItem(loader.getItemType(), data, index);
            listItem.setTag(loader.getItemTag());
            loader.addListItem(listItem);
        }
    }

    public static class Builder {

        HttpListController listManager;

        String url;

        boolean needLoginInfo;

        String cacheKey;

        OnBuildListImpl onBuildListImpl;

        OnLoadImpl onLoadImpl;

        ItemParser itemParser;

        boolean loadMore = true;

        int pageSize = DEFAULT_PAGE_SIZE;

        public Builder(HttpListController listManager) {
            this.listManager = listManager;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setNeedLoginInfo(boolean needLoginInfo) {
            this.needLoginInfo = needLoginInfo;
            return this;
        }

        public Builder setCacheKey(String cacheKey) {
            this.cacheKey = cacheKey;
            return this;
        }

        public Builder setOnBuildListImpl(OnBuildListImpl onBuildListImpl) {
            this.onBuildListImpl = onBuildListImpl;
            return this;
        }

        public Builder setOnLoadImpl(OnLoadImpl onLoadImpl) {
            this.onLoadImpl = onLoadImpl;
            return this;
        }

        public Builder setItemParser(ItemParser itemParser) {
            this.itemParser = itemParser;
            return this;
        }

        public Builder setLoadMore(boolean loadMore) {
            this.loadMore = loadMore;
            return this;
        }

        public Builder setPageSize(int pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public CommonHttpLoader build() {
            CommonHttpLoader requester = new CommonHttpLoader(listManager);
            if (cacheKey != null) {
                requester.mCacheKey = cacheKey;
            }
            if (itemParser != null) {
                requester.mItemParser = itemParser;
            }
            requester.mPageSize = pageSize;
            requester.mItemTag = listManager.getItemTag();
            requester.mItemType = listManager.getItemType();
            requester.mNeedLoginInfo = needLoginInfo;
            requester.mLoadMore = loadMore;
            requester.mUrl = url;
            if (onBuildListImpl == null) {
                onBuildListImpl = new DefaultOnBuildListImpl();
            }
            onBuildListImpl.loader = requester;
            requester.mOnBuildListImpl = onBuildListImpl;
            onLoadImpl.loader = requester;
            requester.mOnLoadImpl = onLoadImpl;
            requester.initCacheData();
            return requester;
        }

    }

}
