package com.github.daweizhou89.okhttpclientutils;

import com.google.gson.Gson;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.exceptions.Exceptions;

/**
 * Created by zhoudawei on 2017/2/27.
 */

public class GsonUtils {

    public static <T> T parse(String response, Type type) {
        T value;
        try {
            value = new Gson().fromJson(response, type);
        } catch (Exception e) {
            DebugLog.e(OkHttpClientUtils.class, "parse", e);
            throw Exceptions.propagate(e);
        }
        if (value == null) {
            value = getInstance(type);
        }
        return value;
    }

    public static <T> T getInstance(Type type) {
        if (type instanceof Class) {
            return getInstance((Class<? extends T>) type);
        } else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            return getInstance(parameterizedType.getRawType());
        }
        return null;
    }

    public static  <T> T parse(String response, Class<T> clazz) {
        T value;
        try {
            value = new Gson().fromJson(response, clazz);
        } catch (Exception e) {
            DebugLog.e(OkHttpClientUtils.class, "parse", e);
            throw Exceptions.propagate(e);
        }
        if (value == null) {
            getInstance(clazz);
        }
        return value;
    }

    public static <T> T getInstance(Class<T> clazz) {
        if (List.class.equals(clazz)) {
            return (T) new ArrayList();
        } else if (Set.class.equals(clazz)) {
            return (T) new HashSet();
        } else if (Map.class.equals(clazz)) {
            return (T) new HashMap();
        }
        try {
            Constructor<T> constructor = clazz.getConstructor();
            return constructor.newInstance();
        } catch (Exception e) {
            DebugLog.e(OkHttpClientUtils.class, "getInstance", e);
        }
        return null;
    }
}
