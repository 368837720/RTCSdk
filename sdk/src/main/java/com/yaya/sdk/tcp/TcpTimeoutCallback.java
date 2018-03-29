package com.yaya.sdk.tcp;

import yaya.tlv.signal.TlvSignal;

/**
 * callback in Yaya Thread
 * Created by ober on 2016/11/9.
 */
public interface TcpTimeoutCallback {
    void onSignalTimeout(TlvSignal req);
}
