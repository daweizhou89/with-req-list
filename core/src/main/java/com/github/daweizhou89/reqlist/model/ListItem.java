package com.github.daweizhou89.reqlist.model;

/**
 * Created by daweizhou89 on 2017/2/17.
 */
public class ListItem {

    public static final int FLAG_NORMAL = -1;
    public static final int FLAG_FIRST = 0;
    public static final int FLAG_LAST = 1;

    private int mType;

    private Object mData;

    private int mFlag;

    private int mIndexOfType;

    private Object mTag;

    public ListItem(int type, Object data) {
        this(type, data, 0);
    }

    public ListItem(int type, Object data, int indexOfType) {
        this(type, data, indexOfType, FLAG_NORMAL);
    }

    public ListItem(int type, Object data, int indexOfType, int flag) {
        mType = type;
        mData = data;
        mIndexOfType = indexOfType;
        mFlag = flag;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        this.mType = type;
    }

    public Object getData() {
        return mData;
    }

    public void setData(Object data) {
        this.mData = data;
    }

    public int getFlag() {
        return mFlag;
    }

    public void setFlag(int flag) {
        this.mFlag = flag;
    }

    public Object getTag() {
        return mTag;
    }

    public void setTag(Object tag) {
        this.mTag = tag;
    }

    public int getIndexOfType() {
        return mIndexOfType;
    }

    public void setIndexOfType(int indexOfType) {
        this.mIndexOfType = indexOfType;
    }

    public static int getFrag(int i, int rowCount) {
        if (i == 0) {
            return ListItem.FLAG_FIRST;
        } else if (i == rowCount - 1) {
            return ListItem.FLAG_LAST;
        }
        return ListItem.FLAG_NORMAL;
    }

    public interface Type {
        int COMMON_ITEM = 1;
        int LOAD_FOOTER = Integer.MAX_VALUE;
    }

}
