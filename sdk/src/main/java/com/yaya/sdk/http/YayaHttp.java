package com.yaya.sdk.http;

import android.content.Context;

import com.yaya.sdk.InitParams;
import com.yaya.sdk.MLog;
import com.yaya.sdk.http.dns.PrefDNSCache;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ober on 2016/10/26.
 */
public class YayaHttp {

    private static final String TAG = "YayaHttp";

    private static final long HTTP_TIME_OUT_DEF = 10000;

    private static YayaHttp instance_;

    public synchronized static YayaHttp getInstance() {
        if(instance_ == null) {
            instance_ = new YayaHttp();
        }
        return instance_;
    }

    private OkHttpClient okHttpClient;

    private YayaHttp() {}

    public void init(Context context, Long timeout, Boolean useDnsCache) {
        if(timeout == null) {
            timeout = HTTP_TIME_OUT_DEF;
        }
        if(useDnsCache == null) {
            useDnsCache = true;
        }
        MLog.d(TAG, "init timeout=" + timeout + ",useDnsCache=" + useDnsCache);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if(useDnsCache) {
            builder.addInterceptor(DnsPolicyInterceptor.create(PrefDNSCache.from(context)));
        }
        builder.readTimeout(timeout, TimeUnit.MILLISECONDS)
                .writeTimeout(timeout, TimeUnit.MILLISECONDS)
                .connectTimeout(timeout, TimeUnit.MILLISECONDS);
        okHttpClient = builder.build();
    }

    public void init(Context context) {
        this.init(context, HTTP_TIME_OUT_DEF, true);
    }

    public OkHttpClient getOkHttp() {
        if(okHttpClient == null) {
            throw new UnsupportedOperationException("instance not init()");
        }
        return okHttpClient;
    }

    public void enqueueWithRetryPolicy(Call call, final Callback callback) {

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MLog.w(TAG, "req onFailure "
                        + e.getClass().getSimpleName() + ": " +e.getMessage());
                MLog.i(TAG, "start retry ");
                Call retryCall = okHttpClient.newCall(call.request());
                retryCall.enqueue(callback);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onResponse(call, response);
            }
        });
    }

}
