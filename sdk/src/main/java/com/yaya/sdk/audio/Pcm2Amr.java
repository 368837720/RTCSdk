package com.yaya.sdk.audio;

import com.yaya.sdk.MLog;
import com.yunva.jni.Native;

import java.io.ByteArrayOutputStream;

/**
 * Created by ober on 2016/11/2.
 */
public class Pcm2Amr {
    private static final String TAG = "Pcm2Amr";

    public interface CodecListener {
        void onFinishCodec(byte[] amrData);
    }

    private static final int ENCODE_BUFFER_INPUT_SIZE = 320;

    private final byte[] mEncodeAmrBuf;

    private final byte[] mEncodePcmBuf;

    private final ByteArrayOutputStream mEncodeResultBuf;

    private CodecListener mCodecListener;

    Pcm2Amr(CodecListener listener) {
        mEncodeAmrBuf = new byte[ENCODE_BUFFER_INPUT_SIZE];
        mEncodePcmBuf = new byte[ENCODE_BUFFER_INPUT_SIZE];
        mEncodeResultBuf = new ByteArrayOutputStream();
        mCodecListener = listener;
    }

    public void openLib() {
        Native.codec_AmrEncoder_open();
    }

    public void closeLib() {
        Native.codec_AmrEncoder_close();
    }

    public void codec(byte[] pcmData) {
        if (pcmData.length != 320) {
            MLog.e(TAG, "pcm长度有误 " + pcmData.length);
        }

        int generatedAmrSize = Native.codec_AmrEncoder_pcm2amr(
                pcmData, pcmData.length,
                mEncodeAmrBuf, mEncodeAmrBuf.length);

        mEncodeResultBuf.write(mEncodeAmrBuf, 0, generatedAmrSize);

        byte[] arr = mEncodeResultBuf.toByteArray();

        if (arr != null && arr.length >= 160) {
            mCodecListener.onFinishCodec(arr);
            mEncodeResultBuf.reset();
        }
    }
}
