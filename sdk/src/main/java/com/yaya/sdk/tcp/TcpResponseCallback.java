package com.yaya.sdk.tcp;

import yaya.tlv.signal.TlvSignal;

/**
 * call back in Yaya Thread
 * Created by ober on 2016/11/9.
 */
public interface TcpResponseCallback {
    void onTcpResponse(TlvSignal req, TlvSignal resp);
}
