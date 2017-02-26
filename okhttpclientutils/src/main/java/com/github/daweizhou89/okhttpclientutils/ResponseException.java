package com.github.daweizhou89.okhttpclientutils;

import java.io.IOException;

/**
 * Created by daweizhou89 on 16/8/4.
 */
public class ResponseException extends IOException {

    public int code;

    public String errorResponse;

    public ResponseException(int code, String errorResponse) {
        super("ResponseException code:" + code + ", errorResponse:" + errorResponse);
        this.code = code;
    }

}
