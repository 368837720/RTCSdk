package com.yaya.sdk.tcp.core;

/**
 * Created by ober on 2016/11/12.
 */
public interface HeartBeatCallback {

    void onWaitHeartBeatResponseTimeout();

    void onHeartBeatResponse(long sendTime, long respTime);
}
