package com.yaya.sdk.tcp.core;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.yaya.sdk.MLog;
import com.yaya.sdk.tcp.ITcp;
import com.yaya.sdk.tcp.SeqUtil;
import com.yaya.sdk.tcp.TcpConfig;
import com.yaya.sdk.tcp.TcpResponseCallback;
import com.yaya.sdk.tcp.TcpTimeoutCallback;
import com.yaya.sdk.tlv.TlvUtil;
import com.yaya.sdk.tlv.protocol.HeartbeatReq;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

import yaya.tlv.header.TlvAccessHeader;
import yaya.tlv.signal.TlvSignal;

/**
 * Created by ober on 2016/11/12.
 */
class RtcHeart {

    private static final String TAG = "RtcHeart";

    private final TlvSignal heartBeatSignalBody;
    private final TlvAccessHeader heartBeatReqHeader;

    private HeartBeatHandler mHandler;

    private HeartBeatCallback mHeartBeatCallback;

    RtcHeart(HeartBeatCallback callback) {
        heartBeatSignalBody = new HeartbeatReq();
        heartBeatReqHeader = TlvUtil.buildHeader(heartBeatSignalBody, 0);
        mHeartBeatCallback = callback;
    }

    public void startBeat(Looper threadLooper) {
        MLog.d(TAG, "startBeat");
        if(mHandler != null) {
            mHandler.stop();
        }
        mHandler = new HeartBeatHandler(threadLooper, this);
        mHandler.start();
    }

    public void stopBeat() {
        MLog.d(TAG, "stopBeat");
        if(mHandler != null) {
            mHandler.stop();
        }
    }

    private void sendHeartBeat() {
        MLog.d(TAG, "sendHeartBeat()");
        ITcp tcp = YayaTcp.getInstance();
        if(tcp.getConnection() == null || !tcp.getConnection().isConnected()) {
            MLog.w(TAG, "connection is closed, stop heart beat");
            mHandler.stop();
            return;
        }

        //reset msgId
        heartBeatReqHeader.setMessageId(SeqUtil.newSeq());
        heartBeatSignalBody.setHeader(heartBeatReqHeader);
        TcpRequest request = new TcpRequest(heartBeatSignalBody);
        LocalTcpHeartBeatListener listener =
                new LocalTcpHeartBeatListener(System.currentTimeMillis());
        request.setTcpResponseCallback(listener);
        request.setTcpTimoutCallback(listener);
        tcp.sendAndListen(request, TcpConfig.HEART_BEAT_TIME_OUT);
        MLog.d(TAG, "sendHeartBeat");
    }

    private class LocalTcpHeartBeatListener implements
            TcpResponseCallback,
            TcpTimeoutCallback {
        long sendTime;
        LocalTcpHeartBeatListener(long sendTime) {
            this.sendTime = sendTime;
        }

        @Override
        public void onTcpResponse(TlvSignal req, TlvSignal resp) {
            if(mHeartBeatCallback != null) {
                mHeartBeatCallback.onHeartBeatResponse(sendTime,
                        System.currentTimeMillis());
            }
        }

        @Override
        public void onSignalTimeout(TlvSignal req) {
            if(mHeartBeatCallback != null) {
                mHeartBeatCallback.onWaitHeartBeatResponseTimeout();
            }
        }
    }

    private static class HeartBeatHandler extends Handler {
        private static final int MSG_HB = 123;

        final RtcHeart self;

        HeartBeatHandler(Looper looper, RtcHeart self) {
            super(looper);
            this.self = self;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == MSG_HB) {
                if(self != null) {
                    self.sendHeartBeat();
                    sendEmptyMessageDelayed(MSG_HB, TcpConfig.HEART_BEAT_PERIOD);
                } else {
                    stop();
                }
            }
        }

        void start() {
            sendEmptyMessageDelayed(MSG_HB, 300);
        }

        void stop() {
            removeMessages(MSG_HB);
        }
    }

}
