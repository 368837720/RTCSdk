package com.yaya.sdk.text;

import android.os.Handler;

import com.yaya.sdk.VideoTroopsRespondListener;
import com.yaya.sdk.YayaRTV;
import com.yaya.sdk.tcp.ResponseDispatcher;
import com.yaya.sdk.tcp.core.TcpRequest;
import com.yaya.sdk.tcp.core.YayaTcp;
import com.yaya.sdk.tlv.protocol.message.TextMessageNotify;

import yaya.tlv.signal.TlvSignal;

/**
 * Created by ober on 2016/11/16.
 */
public class TextMessageDispatcher implements ResponseDispatcher {

    public static ResponseDispatcher create() {
        return new TextMessageDispatcher();
    }

    private Handler mYayaThreadHandler;

    private TextMessageDispatcher() {
        initHandler();
    }

    private void initHandler() {

        if(mYayaThreadHandler == null && YayaTcp.getInstance().getYayaThreadLooper() != null) {
            mYayaThreadHandler = new Handler(YayaTcp
                    .getInstance()
                    .getYayaThreadLooper());
        }
    }

    @Override
    public void dispatch(TlvSignal signal) {
        initHandler();
        final TextMessageNotify textMessageNotify = (TextMessageNotify) signal;
        mYayaThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                final VideoTroopsRespondListener listener = ((YayaRTV)(YayaRTV.getInstance()))
                        .getResponseListener();
                if(listener != null) {
                    listener.onTextMessageNotify(textMessageNotify);
                }
            }
        });
    }
}
