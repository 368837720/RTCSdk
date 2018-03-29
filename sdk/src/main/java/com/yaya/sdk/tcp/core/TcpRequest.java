package com.yaya.sdk.tcp.core;

import com.yaya.sdk.tcp.TcpResponseCallback;
import com.yaya.sdk.tcp.TcpTimeoutCallback;
import com.yaya.sdk.tcp.core.EmptyTcpResponseCallback;
import com.yaya.sdk.tcp.core.EmptyTcpTimeoutCallback;

import yaya.tlv.signal.TlvSignal;

/**
 * Created by ober on 2016/11/9.
 */
public class TcpRequest {
    private final long seqNum;
    private final TlvSignal mTlvSignal;

    private TcpResponseCallback mResponseCallback = EmptyTcpResponseCallback.Singleton;
    private TcpTimeoutCallback mTimeoutCallback = EmptyTcpTimeoutCallback.Singleton;

    public TcpRequest(TlvSignal tlvSignal) {
        if(tlvSignal == null) {
            throw new NullPointerException("null tlvSignal!");
        }
        if(tlvSignal.getHeader() == null) {
            throw new NullPointerException("bad tlvSignal without header!");
        }
        this.seqNum = tlvSignal.getHeader().getMessageId();
        this.mTlvSignal = tlvSignal;
    }

    public TlvSignal getTlvSignal() {
        return mTlvSignal;
    }

    public long getSeqNum() {
        return seqNum;
    }

    public void setTcpResponseCallback(TcpResponseCallback callback) {
        this.mResponseCallback = callback;
    }

    public void setTcpTimoutCallback(TcpTimeoutCallback callback) {
        this.mTimeoutCallback = callback;
    }

    public TcpResponseCallback getTcpResponseCallback() {
        return mResponseCallback;
    }

    public TcpTimeoutCallback getTcpTimeoutCallback() {
        return mTimeoutCallback;
    }

}
