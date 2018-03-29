package com.yaya.sdk.audio.buffer;

import com.yaya.sdk.MLog;
import com.yaya.sdk.tlv.protocol.message.VoiceMessageNotify;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by ober on 2016/12/5.
 */
public class BlockQueueBuffer implements JitterBufferInterface {

    public static JitterBufferInterface newInstance() {
        return new BlockQueueBuffer();
    }

    private BlockingQueue<VoiceMessageNotify> mQueue;

    BlockQueueBuffer() {
        mQueue = new ArrayBlockingQueue<>(10);
    }

    @Override
    public void initJitter() {
        mQueue.clear();
    }

    @Override
    public void destroyJitter() {
        mQueue.clear();
    }

    @Override
    public int jitterSize() {
        return mQueue.size();
    }

    @Override
    public void jitterPut(VoiceMessageNotify voiceMessageNotify) {
        boolean r = mQueue.offer(voiceMessageNotify);
        if(!r) {
            MLog.d("ReceiveBuffer", "receive blocked");
        }
    }

    @Override
    public VoiceMessageNotify jitterTake() throws InterruptedException {
        return mQueue.take();
    }

    @Override
    public void jitterUpdateDelay() {

    }

    @Override
    public void jitterTick() {

    }

    @Override
    public boolean checkInitialized() {
        return true;
    }
}
