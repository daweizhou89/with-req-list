package com.github.daweizhou89.reqlist.manager;

/**
 * ****  ****  *  ****
 * *  *  *  *     *
 * ****  *  *  *     *
 * *     *  *  *     *
 * ****  ****  *     *
 * <p>
 * Created by daweizhou89 on 2017/1/23.
 */

public class RequestingTypeGenerator {

    private int mLastRequestingType;

    private static RequestingTypeGenerator sInstance;

    public static RequestingTypeGenerator getInstance() {
        if (sInstance == null) {
            sInstance = new RequestingTypeGenerator();
        }
        return sInstance;
    }

    private RequestingTypeGenerator() {
    }

    public int generateRequestingType() {
        int newRequestingType = mLastRequestingType + 1;
        if (newRequestingType > AbstractListManager.MSG_MAX_REQUESTING_TYPE) {
            newRequestingType = 1;
        }
        mLastRequestingType = newRequestingType;
        return newRequestingType;
    }

}
