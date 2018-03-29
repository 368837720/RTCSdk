package com.yaya.sdk.tcp.core;

import com.yaya.sdk.MLog;
import com.yaya.sdk.tcp.TcpTimeoutCallback;

import yaya.tlv.signal.TlvSignal;

/**
 * Created by ober on 2016/11/9.
 */
public class EmptyTcpTimeoutCallback implements TcpTimeoutCallback {

    private static final String TAG = "EmptyTcpTimeoutCallback";

    public static final TcpTimeoutCallback Singleton = new EmptyTcpTimeoutCallback();

    private EmptyTcpTimeoutCallback() {}

    @Override
    public void onSignalTimeout(TlvSignal signal) {
        MLog.w(TAG, "onSignalTimeout " + signal.toString());
    }
}
