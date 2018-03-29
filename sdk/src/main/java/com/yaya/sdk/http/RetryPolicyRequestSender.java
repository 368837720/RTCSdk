package com.yaya.sdk.http;

import okhttp3.Call;

/**
 * Created by ober on 2016/11/14.
 */
public interface RetryPolicyRequestSender {

    void sendRequest(Call call);
}
