package com.yaya.sdk.tcp.core;

import com.yaya.sdk.MLog;
import com.yaya.sdk.tcp.TcpResponseCallback;

import yaya.tlv.signal.TlvSignal;

/**
 * Created by ober on 2016/11/9.
 */
public class EmptyTcpResponseCallback implements TcpResponseCallback {
    private static final String TAG = "EmptyTcpResponseCallback";

    public static final TcpResponseCallback Singleton = new EmptyTcpResponseCallback();

    private EmptyTcpResponseCallback() {}

    @Override
    public void onTcpResponse(TlvSignal req, TlvSignal resp) {
        MLog.d(TAG, "onTcpResponse " + resp.toString());
    }
}
