package com.github.daweizhou89.okhttpclientutils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.text.MessageFormat;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by daweizhou89 on 16/8/4.
 */
public class OkHttpClientUtils {

    private static OkHttpClient sOkHttpClient;

    private static String sUserAgent;

    public static void init(String userAgent) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        sOkHttpClient = builder.build();
        sUserAgent = userAgent;
    }

    public static String getUserAgent() {
        return sUserAgent;
    }

    public static void get(@NonNull final String url, @Nullable final RequestParams params, @Nullable final TextResponseCallback callback) {
        get(url, params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        if (callback != null) {
                            callback.onSuccess(url, response);
                        } else {
                            if (DebugLog.DEBUG) {
                                DebugLog.d(OkHttpClientUtils.class, "get", "onSuccess(no callback):" + response);
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (callback != null) {
                            callback.onFailure(url, throwable);
                        } else {
                            if (DebugLog.DEBUG) {
                                DebugLog.e(OkHttpClientUtils.class, "get", "onFailure(no callback)", throwable);
                            }
                        }
                    }
                });
    }

    /***
     * Rxjava执行请求
     *
     * @param url
     * @param params
     * @return
     */
    public static <T> Observable<T> get(@NonNull final String url, @Nullable final RequestParams params, @NonNull final Class<T> clazz) {
        if (DebugLog.DEBUG) {
            DebugLog.d(OkHttpClientUtils.class, "get", MessageFormat.format("{0}?{1}", url, params == null ? null : params.toString()));
        }
        final Request request = createGetRequest(url, params);
        return createObservable(request)
                .observeOn(Schedulers.io())
                .map(new Function<String, T>() {
                    @Override
                    public T apply(String response) {
                        T value = GsonUtils.parse(response, clazz);
                        return value;
                    }
                });
    }

    /***
     * Rxjava执行请求
     *
     * @param url
     * @param params
     * @return
     */
    public static <T> Observable<T> get(@NonNull final String url, @Nullable final RequestParams params, @NonNull final Type type) {
        if (DebugLog.DEBUG) {
            DebugLog.d(OkHttpClientUtils.class, "get(Type)", MessageFormat.format("{0}?{1}", url, params == null ? null : params.toString()));
        }
        final Request request = createGetRequest(url, params);
        return createObservable(request)
                .observeOn(Schedulers.io())
                .map(new Function<String, T>() {
                    @Override
                    public T apply(String response) {
                        T value = GsonUtils.parse(response, type);
                        return value;
                    }
                });
    }

    /***
     * Rxjava执行请求
     *
     * @param url
     * @param params
     * @return
     * @throws IOException
     */
    public static Observable<String> get(@NonNull final String url, @Nullable final RequestParams params) {
        if (DebugLog.DEBUG) {
            DebugLog.d(OkHttpClientUtils.class, "get", MessageFormat.format("{0}?{1}", url, params == null ? null : params.toString()));
        }
        final Request request = createGetRequest(url, params);
        return createObservable(request);
    }

    public static void post(@NonNull final String url, @Nullable final RequestParams params, @Nullable final TextResponseCallback callback) {
        if (Config.sAssert) {
            Assert.assertMainThread();
        }
        post(url, params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        if (callback != null) {
                            callback.onSuccess(url, response);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (callback != null) {
                            callback.onFailure(url, throwable);
                        }
                    }
                });

    }

    /***
     * Rxjava执行请求
     *
     * @param url
     * @param params
     * @return
     */
    public static <T> Observable<T> post(@NonNull final String url, @Nullable final RequestParams params, @NonNull final Class<T> clazz) {
        if (DebugLog.DEBUG) {
            DebugLog.d(OkHttpClientUtils.class, "post", MessageFormat.format("{0}?{1}", url, params == null ? null : params.toString()));
        }
        final Request request = createPostRequest(url, params);

        return createObservable(request)
                .observeOn(Schedulers.io())
                .map(new Function<String, T>() {
                    @Override
                    public T apply(String response) {
                        T value = GsonUtils.parse(response, clazz);
                        return value;
                    }
                });
    }

    /***
     * Rxjava执行请求
     *
     * @param url
     * @param params
     * @return
     */
    public static <T> Observable<T> post(@NonNull String url, @Nullable final RequestParams params, @NonNull final Type type) {
        if (DebugLog.DEBUG) {
            DebugLog.d(OkHttpClientUtils.class, "post(Type)", MessageFormat.format("{0}?{1}", url, params == null ? null : params.toString()));
        }
        final Request request = createPostRequest(url, params);

        return createObservable(request)
                .observeOn(Schedulers.io())
                .map(new Function<String, T>() {
                    @Override
                    public T apply(String response) {
                        T value = GsonUtils.parse(response, type);
                        return value;
                    }
                });
    }

    /***
     * Rxjava执行请求
     *
     * @param url
     * @param body
     * @param clazz
     * @return
     */
    public static <T> Observable<T> post(@NonNull final String url, @NonNull final RequestBody body, @NonNull final Class<T> clazz) {
        if (DebugLog.DEBUG) {
            DebugLog.d(OkHttpClientUtils.class, "post(RequestBody)", "url:" + url);
        }
        final Request request = createPostRequest(url, body);

        return createObservable(request)
                .observeOn(Schedulers.io())
                .map(new Function<String, T>() {
                    @Override
                    public T apply(String response) {
                        T value = GsonUtils.parse(response, clazz);
                        return value;
                    }
                });
    }

    /***
     * Rxjava执行请求
     *
     * @param url
     * @param body
     * @param type
     * @return
     */
    public static <T> Observable<T> post(@NonNull final String url, @NonNull final RequestBody body, @NonNull final Type type) {
        if (DebugLog.DEBUG) {
            DebugLog.d(OkHttpClientUtils.class, "post(RequestBody, Type)", "url:" + url);
        }
        final Request request = createPostRequest(url, body);

        return createObservable(request)
                .observeOn(Schedulers.io())
                .map(new Function<String, T>() {
                    @Override
                    public T apply(String response) {
                        T value = GsonUtils.parse(response, type);
                        return value;
                    }
                });
    }

    /***
     * 同步执行请求
     *
     * @param url
     * @param params
     * @return
     * @throws IOException
     */
    public static Observable<String> post(@NonNull String url, @Nullable final RequestParams params) {
        if (DebugLog.DEBUG) {
            DebugLog.d(OkHttpClientUtils.class, "post(RequestParams)", MessageFormat.format("{0}?{1}", url, params == null ? null : params.toString()));
        }
        final Request request = createPostRequest(url, params);

        return createObservable(request);
    }

    /***
     * 进行请求RequestBody
     *
     * @param url
     * @param body
     */
    public static Observable<String> post(@NonNull final String url, @NonNull final RequestBody body) {
        if (DebugLog.DEBUG) {
            DebugLog.d(OkHttpClientUtils.class, "post(RequestBody)", "url:" + url);
        }
        final Request request = createPostRequest(url, body);

        return createObservable(request);
    }

    private static Request createPostRequest(String url, RequestParams params) {
        return createPostRequest(url, params == null ? null : params.getRequestBody());
    }

    private static Request createPostRequest(String url, RequestBody body) {
        final Request.Builder builder = new Request.Builder();
        if (!TextUtils.isEmpty(sUserAgent)) {
            builder.addHeader("User-Agent", sUserAgent);
        }
        builder.url(url);
        if (body != null) {
            builder.post(body);
        }
        return builder.build();
    }

    private static Request createGetRequest(String url, final RequestParams params) {
        String targetUrl = url;
        if (params != null) {
            targetUrl = getUrlWithQuery(url, params.toString());
        }
        final Request.Builder builder = new Request.Builder();
        if (!TextUtils.isEmpty(sUserAgent)) {
            builder.addHeader("User-Agent", sUserAgent);
        }
        builder.url(targetUrl);
        return builder.build();
    }

    private static Observable<String> createObservable(final Request request) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                if (DebugLog.DEBUG) {
                    DebugLog.d(getClass(), "subscribe", "rx-thread:" + Thread.currentThread().toString());
                }
                Response response = null;
                try {
                    response = sOkHttpClient.newCall(request).execute();
                    checkResponseSuccessful(response);
                    final String bodyString = response.body().string();
                    if (!emitter.isDisposed()) {
                        emitter.onNext(bodyString != null ? bodyString : "");
                        emitter.onComplete();
                    }
                } catch (Exception e) {
                    DebugLog.e(OkHttpClientUtils.class, "createObservable", e);
                    emitter.onError(e);
                } finally {
                    safeClose(response);
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    public static void checkResponseSuccessful(Response response) throws IOException {
        if (!response.isSuccessful()) {
            final ResponseBody responseBody = response.body();
            final String errorContent = responseBody == null ? null : responseBody.toString();
            IOException exception = new ResponseException(response.code(), errorContent);
            throw exception;
        }
    }

    public static void safeClose(Response response) {
        if (response != null) {
            try {
                response.close();
            } catch (Exception e) {
                DebugLog.e(OkHttpClientUtils.class, "safeClose", e);
            }
        }
    }

    public static String getUrlWithQuery(@NonNull String url, String params) {
        if (url != null && params != null && !params.equals("") && !params.equals("?")) {
            StringBuilder sb = new StringBuilder()
                    .append(url)
                    .append(url.contains("?") ? "&" : "?")
                    .append(params);
            return sb.toString();
        }
        return url;
    }

    public static boolean isHttpResponseForbidden(Throwable throwable) {
        if (throwable != null && throwable instanceof ResponseException) {
            ResponseException responseException = (ResponseException) throwable;
            if (responseException.code == HttpURLConnection.HTTP_FORBIDDEN) {
                return true;
            }
        }
        return false;
    }
}
