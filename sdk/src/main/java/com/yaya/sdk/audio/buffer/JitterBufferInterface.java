package com.yaya.sdk.audio.buffer;

import com.yaya.sdk.tlv.protocol.message.VoiceMessageNotify;

/**
 * Created by ober on 2016/11/27.
 */
public interface JitterBufferInterface {

    void initJitter();

    void destroyJitter();

    int jitterSize();

    void jitterPut(VoiceMessageNotify voiceMessageNotify);

    VoiceMessageNotify jitterTake() throws InterruptedException;

    void jitterUpdateDelay();

    void jitterTick();

    boolean checkInitialized();
}
