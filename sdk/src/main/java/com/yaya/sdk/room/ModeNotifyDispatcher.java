package com.yaya.sdk.room;

import android.os.Handler;

import com.yaya.sdk.RTV;
import com.yaya.sdk.VideoTroopsRespondListener;
import com.yaya.sdk.YayaRTV;
import com.yaya.sdk.account.AccountState;
import com.yaya.sdk.tcp.ResponseDispatcher;
import com.yaya.sdk.tcp.core.YayaTcp;
import com.yaya.sdk.tlv.protocol.ModeChangeNotify;

import yaya.tlv.signal.TlvSignal;

/**
 * Created by ober on 2016/12/5.
 */
public class ModeNotifyDispatcher implements ResponseDispatcher {

    public static ResponseDispatcher create() {
        return new ModeNotifyDispatcher();
    }

    private Handler mYayaThreadHandler;

    ModeNotifyDispatcher() {
        mYayaThreadHandler = new Handler(YayaTcp
                .getInstance()
                .getYayaThreadLooper());
    }

    @Override
    public void dispatch(TlvSignal signal) {
        final ModeChangeNotify notify = (ModeChangeNotify) signal;
        mYayaThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                final VideoTroopsRespondListener listener = ((YayaRTV)(YayaRTV.getInstance()))
                        .getResponseListener();
                if(listener != null) {
                    byte bMode = notify.getMode();
                    RTV.Mode mode;
                    if(bMode == 0) {
                        mode = RTV.Mode.Free;
                    } else {
                        mode = RTV.Mode.Robmic;
                    }
                    boolean isLeader = false;
                    if(notify.getLeaderMode() == 1) {
                        mode = RTV.Mode.Leader;
                        long yunvaId = AccountState.getInstance().getYunvaId();
                        if(notify.getYunvaId() == yunvaId) {
                            isLeader = true;
                        }
                    }

                    listener.onTroopsModeChangeNotify(mode, isLeader);
                }
            }
        });
    }
}
