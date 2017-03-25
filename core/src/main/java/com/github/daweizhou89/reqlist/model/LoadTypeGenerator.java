package com.github.daweizhou89.reqlist.model;

/**
 * ****  ****  *  ****
 * *  *  *  *     *
 * ****  *  *  *     *
 * *     *  *  *     *
 * ****  ****  *     *
 * <p>
 * Created by daweizhou89 on 2017/1/23.
 */

public class LoadTypeGenerator {

    /***
     * 最大的请求类型，大于0xFF是普通Handler消息
     */
    public final static int MSG_MAX_REQUESTING_TYPE = 0xFF;

    private int mLastRequestingType;

    private static LoadTypeGenerator sInstance;

    public static LoadTypeGenerator getInstance() {
        if (sInstance == null) {
            sInstance = new LoadTypeGenerator();
        }
        return sInstance;
    }

    private LoadTypeGenerator() {
    }

    public int generateLoadType() {
        int newRequestingType = mLastRequestingType + 1;
        if (newRequestingType > MSG_MAX_REQUESTING_TYPE) {
            newRequestingType = 1;
        }
        mLastRequestingType = newRequestingType;
        return newRequestingType;
    }

}
