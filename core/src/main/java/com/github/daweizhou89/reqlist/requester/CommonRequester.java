package com.github.daweizhou89.reqlist.requester;

import android.support.annotation.NonNull;

import com.github.daweizhou89.reqlist.DebugLog;
import com.github.daweizhou89.reqlist.manager.AbstractListManager;
import com.github.daweizhou89.reqlist.manager.CommonListManager;
import com.github.daweizhou89.reqlist.model.ListItem;

import java.util.List;

/**
 * Created by daweizhou89 on 16/6/29.
 */
public final class CommonRequester extends AbstractRequester {

    protected String mUrl;

    protected boolean mNeedLoginInfo;

    protected String mItemTag;

    protected int mItemType;

    protected String mCacheKey;

    protected OnBuildListImpl mOnBuildListImpl;

    protected OnRequestImpl mOnRequestImpl;

    protected ItemParser mItemParser;

    public CommonRequester(@NonNull AbstractListManager listManager) {
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
    protected void onRequest(int pageNo, CallBack callback, Object... inputs) {
        mOnRequestImpl.onRequest(pageNo, callback, inputs);
    }

    @Override
    protected void onRequestMore(int morePageNo, MoreCallback callBack, Object... inputs) {
        mOnRequestImpl.onRequestMore(morePageNo, callBack, inputs);
    }

    @Override
    protected void onResponse(String url, String response) {
        if (mItemParser != null) {
            List items = mItemParser.parseItems(response);
            setData(items);
        }
    }

    @Override
    protected void onResponseMore(String url, String response) {
        if (mItemParser != null) {
            List items = mItemParser.parseItems(response);
            appendData(items);
        }
    }

    @Override
    protected void onBuild(Object... inputs) {
        mOnBuildListImpl.onBuild(inputs);
    }

    @Override
    public void appendData(List data) {
        if (mItemParser != null) {
            mItemParser.beforeAppendItems(data);
        }
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

        protected CommonRequester requester;

        public abstract void onBuild(Object... inputs);

        public CommonRequester getRequester() {
            return requester;
        }

    }

    public static abstract class OnRequestImpl {

        protected CommonRequester requester;

        public abstract void onRequest(int pageNo, CallBack callBack, Object... inputs);

        public abstract void onRequestMore(int morePageNo, MoreCallback callback, Object... inputs);

        public CommonRequester getRequester() {
            return requester;
        }
    }

    public static abstract class ItemParser {
        public abstract List parseItems(String response);

        public void beforeAppendItems(List datas) {
            // TODO nothing
        }
    }

    public static class DefaultOnBuildListImpl extends OnBuildListImpl {

        @Override
        public void onBuild(Object... inputs) {
            List data = requester.getData();
            buildDatas(data, 0, data == null ? 0 : data.size());
        }

        private void buildDatas(List datas, int startIndex, int length) {
            if (datas == null || datas.isEmpty()) {
                return;
            }
            for (int i = startIndex; i < startIndex + length; i++) {
                buildDataInternal(datas.get(i), i);
            }
        }

        private void buildDataInternal(Object data, int index) {
            final ListItem listItem = new ListItem(requester.getItemType(), data, index);
            listItem.setTag(requester.getItemTag());
            requester.addListItem(listItem);
        }
    }

    public static class Builder {

        CommonListManager listManager;

        String url;

        boolean needLoginInfo;

        String cacheKey;

        OnBuildListImpl onBuildListImpl;

        OnRequestImpl onRequestImpl;

        ItemParser itemParser;

        boolean loadMoreEnable = true;

        int pageSize = DEFAULT_PAGE_SIZE;

        public Builder(CommonListManager listManager) {
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

        public Builder setOnRequestImpl(OnRequestImpl onRequestImpl) {
            this.onRequestImpl = onRequestImpl;
            return this;
        }

        public Builder setItemParser(ItemParser itemParser) {
            this.itemParser = itemParser;
            return this;
        }

        public Builder setLoadMoreEnable(boolean loadMoreEnable) {
            this.loadMoreEnable = loadMoreEnable;
            return this;
        }

        public Builder setPageSize(int pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public CommonRequester build() {
            CommonRequester requester = new CommonRequester(listManager);
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
            requester.mLoadMoreEnable = loadMoreEnable;
            requester.mUrl = url;
            if (onBuildListImpl == null) {
                onBuildListImpl = new DefaultOnBuildListImpl();
            }
            onBuildListImpl.requester = requester;
            requester.mOnBuildListImpl = onBuildListImpl;
            onRequestImpl.requester = requester;
            requester.mOnRequestImpl = onRequestImpl;
            requester.initCacheData();
            return requester;
        }

    }

}
