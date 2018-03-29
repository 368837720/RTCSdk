package com.yaya.sdk.audio;


import com.yaya.sdk.MLog;
import com.yaya.sdk.audio.buffer.JitterBufferInterface;
import com.yaya.sdk.tcp.ResponseDispatcher;
import com.yaya.sdk.tlv.protocol.message.VoiceMessageNotify;
import com.yaya.sdk.tlv.protocol.message.VoiceMessageResp;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

import yaya.tlv.signal.TlvSignal;

/**
 * Created by ober on 2016/11/10.
 */
public class VoiceMsgRespDispatcher implements ResponseDispatcher {

    private static final String TAG = "VoiceMsgDispatcher";

    public interface VoiceMsgDispatchCallback {
        void onVoiceMsgDispatch(long yunvaId, String troopsId, String expand);

        void onVoiceCacheBlocked();

        void onSendResponseDispatch(long code, String msg); // 发送语音消息的resp（失败）
    }

    private VoiceMsgDispatchCallback mDispatchCallback;

    private final JitterBufferInterface mJitterBuffer;

    public VoiceMsgRespDispatcher(JitterBufferInterface jitterBuffer, VoiceMsgDispatchCallback callback) {
        mJitterBuffer = jitterBuffer;
        mDispatchCallback = callback;
    }

    @Override
    public void dispatch(TlvSignal signal) {
        if (signal instanceof VoiceMessageNotify) {
            final VoiceMessageNotify notify = (VoiceMessageNotify) signal;

            int pcmLength = notify.getMsg().length;

            if(pcmLength != 160) {
                MLog.e(TAG, "!!!VoiceMessageNotify Err Data Length!!!---" + pcmLength);
                return;
            }

            final VoiceMsgDispatchCallback callback = mDispatchCallback;

            if(mJitterBuffer.jitterSize() > 10) {
                MLog.w(TAG, "jitter buffer may be blocked");
                if (callback != null) {
                    callback.onVoiceCacheBlocked();
                }
            }

            mJitterBuffer.jitterPut(notify);

            if(callback != null) {
                callback.onVoiceMsgDispatch(notify.getYunvaId(),
                        notify.getTroopsId(), notify.getExpand());
            }

        } else if (signal instanceof VoiceMessageResp) {
            if (mDispatchCallback != null) {
                VoiceMessageResp resp = (VoiceMessageResp) signal;
                mDispatchCallback.onSendResponseDispatch(resp.getResult(), resp.getMsg());
            }
        }
    }
}
