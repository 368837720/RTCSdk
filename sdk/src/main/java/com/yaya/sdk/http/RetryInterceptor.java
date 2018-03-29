package com.yaya.sdk.http;

import com.yaya.sdk.MLog;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ober on 2016/11/14.
 */
class RetryInterceptor implements Interceptor {

    private static final String TAG = "RetryInterceptor";

    private final int retryCount;

    //requestSeq -- retryCount
    private Map<String, Integer> mInflightRequestState;

    public static Interceptor forRetryCount(int count) {
        return new RetryInterceptor(count);
    }

    private RetryInterceptor(int retryCount) {
        this.retryCount = retryCount;
        mInflightRequestState = new ConcurrentHashMap<>();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String seq = request.header("msgId");
        MLog.d(TAG, "proceed request seq=" + seq);
        Response response = chain.proceed(request);
        while (!response.isSuccessful()) {
            //请求失败
            if(mInflightRequestState.containsKey(seq)) {
                int retryCount = mInflightRequestState.get(seq); //获取该请求已重试次数
                MLog.i(TAG, "retryCount get = " + retryCount);
                if(retryCount >= this.retryCount) {
                    MLog.w(TAG, "retry max, return this errResp");
                    return response;
                } else {
                    mInflightRequestState.put(seq, retryCount + 1);
                    MLog.i(TAG, "retry proceed request");
                    response = chain.proceed(request);
                }
            } else {
                mInflightRequestState.put(seq, 1);
                MLog.i(TAG, "retry proceed request");
                response = chain.proceed(request);
            }
        }

        mInflightRequestState.remove(seq);

        return response;
    }
}
