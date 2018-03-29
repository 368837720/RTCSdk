package com.yaya.sdk.audio.buffer;

import com.yaya.sdk.MLog;
import com.yaya.sdk.tlv.protocol.message.VoiceMessageNotify;
import com.yunva.jni.JitterBufferPacket;
import com.yunva.jni.Native;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by ober on 2016/11/27.
 */
public class YayaNativeJitterBuffer implements JitterBufferInterface {

    private static final String TAG = "YayaJitterBuf";

    private Queue<VoiceMessageNotify> mReceivedDataQueue;

    private long mNativeJitterBufferPointer = -1;

    private final Object mJitterLock;

    private final int[] mCurrTimestamp;

    private AtomicInteger atomicInteger;

    private boolean isInit;

    public static JitterBufferInterface newInstance() {
        return new YayaNativeJitterBuffer();
    }

    YayaNativeJitterBuffer() {
        mJitterLock = new Object();
        mReceivedDataQueue = new LinkedList<>();
        mCurrTimestamp = new int[1];
    }

    @Override
    public void initJitter() {
        MLog.d(TAG, "initJitter");
        if(isInit) {
            MLog.w(TAG, "duplicate init");
            return;
        }
        isInit = true;
        mReceivedDataQueue.clear();
        mNativeJitterBufferPointer = Native.audio_buffer_init(160);
        Native.audio_buffer_ctl(mNativeJitterBufferPointer, 0, new int[] {5 * 160});
        atomicInteger = new AtomicInteger(0);
    }

    @Override
    public void destroyJitter() {
        MLog.d(TAG, "destroyJitter");
        if(!isInit) {
            MLog.w(TAG, "destroy before init");
            return;
        }
        mReceivedDataQueue.clear();
        Native.audio_buffer_destroy(mNativeJitterBufferPointer);
        mNativeJitterBufferPointer = -1;
        isInit = false;
    }

    @Override
    public int jitterSize() {
        if(!isInit) {
            return 0;
        }
        synchronized (mJitterLock) {
            return mReceivedDataQueue.size();
        }
    }

    @Override
    public void jitterPut(VoiceMessageNotify voiceMessageNotify) {
        if(!checkInitialized()) {
            return;
        }
        final byte[] data = voiceMessageNotify.getMsg();
        if(data.length != 160) {
            MLog.e(TAG, "!!!VoiceMessageNotify Err Length!!!---" + data.length);
            return;
        }
        voiceMessageNotify.setMsg(null); //amr data byte[] 在jitter buffer中处理，这里不需要引用, 优化内存

        final int seq = atomicInteger.getAndIncrement();
        final JitterBufferPacket jbp = new JitterBufferPacket();
        jbp.data = data;
        jbp.len = 160;
        jbp.timestamp = seq * 160;
        jbp.span = 160;
        synchronized (mJitterLock) {
            Native.audio_buffer_put(mNativeJitterBufferPointer, jbp);

            mReceivedDataQueue.offer(voiceMessageNotify);
            mJitterLock.notifyAll();
        }

    }

    /**
     * 可能返回null?
     */
    @Override
    public VoiceMessageNotify jitterTake() throws InterruptedException {
        if(!checkInitialized()) {
            return null;
        }
        synchronized (mJitterLock) {
            while (mReceivedDataQueue.size() == 0) {
                mJitterLock.wait();
            }
        }

        JitterBufferPacket jbp = new JitterBufferPacket();
        jbp.data = new byte[160];
        jbp.len = jbp.data.length;

        int get;

        synchronized (mJitterLock) {
            get = Native.audio_buffer_get(mNativeJitterBufferPointer, jbp, 160, mCurrTimestamp);
            //get=0表示获取到,其它标识未获取到
        }

        if(get != 0) {
            return null;
        }

        VoiceMessageNotify notify = mReceivedDataQueue.poll();
        notify.setMsg(jbp.data);

        return notify;
    }

    @Override
    public void jitterUpdateDelay() {
        if(!checkInitialized()) {
            return;
        }
        synchronized (mJitterLock) {
            Native.audio_buffer_update_delay(mNativeJitterBufferPointer, null, null);
        }
    }

    @Override
    public void jitterTick() {
        if(!checkInitialized()) {
            return;
        }
        synchronized (mJitterLock) {
            Native.audio_buffer_tick(mNativeJitterBufferPointer);
        }
    }

    @Override
    public boolean checkInitialized() {
        if(mNativeJitterBufferPointer == -1 || !isInit) {
            MLog.e(TAG, "Jitter Not Initialized");
            return false;
        }
        return true;
    }
}
