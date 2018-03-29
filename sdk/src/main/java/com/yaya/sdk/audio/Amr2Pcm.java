package com.yaya.sdk.audio;

import com.yaya.sdk.MLog;
import com.yaya.sdk.util.ArrayUtils;
import com.yunva.jni.Native;

/**
 * Created by ober on 2016/11/2.
 */
public class Amr2Pcm {

    private static final String TAG = "Amr2Pcm";

    private static final int RECEIVE_AMR_LENGTH = 160;

    private static final int CODEC_FRAME_LENGTH = 32;

    public interface CodecListener {
        //160长度的pcm
        void onFinishCodec(short[] pcmData, long yunvaId, String troopsId, String expand);
    }

    private final int mLoopCount;

    private final int[] consumeAndOutSize = new int[2];

    private final byte[] tempAmr = new byte[CODEC_FRAME_LENGTH];
    private final short[] tempPcm = new short[CODEC_FRAME_LENGTH * 10];

    private final CodecListener mCodecListener;

    public Amr2Pcm(CodecListener listener) {
        mLoopCount = RECEIVE_AMR_LENGTH / CODEC_FRAME_LENGTH;
        if(RECEIVE_AMR_LENGTH % CODEC_FRAME_LENGTH != 0) {
            //如果不能整除，需要添加remainingBuffer, 当前刚好5个
        }
        mCodecListener = listener;
    }

    public static void openLib() {
        Native.codec_AmrDecoder_open();
    }

    public static void closeLib() {
        Native.codec_AmrDecoder_close();
    }

    //160byte amr,一次解32b, listener会callback 5次
    public void codec(byte[] amr, long yunvaId, String troopsId, String expand) {
        if(amr == null) {
            MLog.e(TAG, "codec err null amr");
            return;
        }

        if(amr.length != RECEIVE_AMR_LENGTH) {
            MLog.e(TAG, "协议返回长度变化，需重写 arm.length=" + amr.length);
            return;
        }

        for(int i = 0; i < mLoopCount; i++) {
            System.arraycopy(amr, i * CODEC_FRAME_LENGTH, tempAmr, 0, CODEC_FRAME_LENGTH);

            //32byte amr -> 320short pcm
            Native.codec_AmrDecoder_amr2pcm(tempAmr, tempAmr.length,
                    tempPcm, tempPcm.length, consumeAndOutSize);
            if(consumeAndOutSize[0] != CODEC_FRAME_LENGTH) {
                MLog.e(TAG, "amr2pcm 长度变化，逻辑有误 " + consumeAndOutSize[0] + "," + consumeAndOutSize[1]);
                return;
            }

            short[] out = new short[CODEC_FRAME_LENGTH * 5];
            //320 pcm

            Native.audio_process_runx(tempPcm, 160, out, 160);

            mCodecListener.onFinishCodec(out, yunvaId, troopsId, expand);
        }

    }
}
