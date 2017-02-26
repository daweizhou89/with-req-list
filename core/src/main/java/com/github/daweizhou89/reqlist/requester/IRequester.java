package com.github.daweizhou89.reqlist.requester;

/**
 * Created by daweizhou89 on 16/6/29.
 */
public interface IRequester {

    void request(Object... inputs);

    void requestMore(Object... inputs);

    void build(Object... inputs);

    boolean isEmpty();

    int getRequestingType();

}
