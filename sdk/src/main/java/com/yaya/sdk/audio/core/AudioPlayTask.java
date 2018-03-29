package com.yaya.sdk.audio.core;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Process;

import com.yaya.sdk.MLog;
import com.yaya.sdk.YayaRTV;
import com.yaya.sdk.audio.Amr2Pcm;
import com.yaya.sdk.audio.buffer.JitterBufferInterface;
import com.yaya.sdk.tlv.protocol.message.VoiceMessageNotify;

/**
 * 播放task
 * Created by ober on 2016/11/1.
 */
public class AudioPlayTask implements Runnable {

    private static final String TAG = "AudioPlayTask";

    private volatile boolean isPlaying;

    private static final int SAMPLE_SIZE_IN_HZ = 8000;
    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_OUT_MONO;
    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    private static final int STREAM_TYPE = AudioManager.STREAM_VOICE_CALL;
    private static final int MODE = AudioTrack.MODE_STREAM;

    private final int bufferSizeInBytes;
    private final Echo echo;

    private AudioTrack audioTrack;

    private static final int FRAME_SIZE = 160;

    private final JitterBufferInterface mJitterBuffer;

    private int missedFrames = 0;

    private final Amr2Pcm amr2Pcm;

    public AudioPlayTask(JitterBufferInterface jitterBuffer) {
        echo = AudioWebrtcTool.m_ec;

        int minBufferSize = AudioTrack.getMinBufferSize(SAMPLE_SIZE_IN_HZ,
                CHANNEL_CONFIG, AUDIO_FORMAT);
        int desiredBufferSize = minBufferSize * 2;
        int frameCount = (int) Math.ceil((double) desiredBufferSize / 320.0D);
        bufferSizeInBytes = frameCount * 320;
        this.mJitterBuffer = jitterBuffer;

        this.amr2Pcm = new Amr2Pcm(new LocalCodecListener());
    }

    public synchronized void stop() {
        MLog.d(TAG, "stop");
        isPlaying = false;
    }

    public synchronized boolean isPlaying() {
        return isPlaying;
    }

    private void initAudioTrack() {
        audioTrack = new AudioTrack(STREAM_TYPE, SAMPLE_SIZE_IN_HZ, CHANNEL_CONFIG,
                AUDIO_FORMAT, bufferSizeInBytes, MODE);
    }

    private class LocalCodecListener implements Amr2Pcm.CodecListener {

        @Override
        public void onFinishCodec(short[] pcmData, long yunvaId, String troopsId, String expand) {
            audioTrack.write(pcmData, 0, 160);
            synchronized (AudioWebrtcTool.ECHO_LOCK) {
                if (echo != null) {
                    echo.echoFarData(pcmData);
                }
            }
            if (!isAudioTrackStart) {
                buffered += pcmData.length;
                if (buffered >= bufferSizeInBytes) {
                    audioTrack.play();
                    isAudioTrackStart = true;
                    buffered = 0;
                    MLog.i(TAG, "Enough data buffered. Starting audio.");
                } else {
                    MLog.i(TAG,
                            "Enough data buffered is < bufferSizeInBytes.");
                }
            }
        }
    }

    private boolean isAudioTrackStart;
    int buffered = 0;

    //如果有耳机，设置听筒模式，如果没有连耳机，设置免提模式
    private void initAudioState() {
        Context c = ((YayaRTV)(YayaRTV.getInstance())).getContext();
        AudioManager audioManager = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
        boolean isWiredHeadsetOn = audioManager.isWiredHeadsetOn();
        MLog.d(TAG, "isWiredHeadsetOn = " + isWiredHeadsetOn);
        audioManager.setSpeakerphoneOn(!isWiredHeadsetOn);
    }

    @Override
    public void run() {

        MLog.d(TAG, "start run() tid=" + Thread.currentThread().getId());
        Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO);

        initAudioState();

        initAudioTrack();
        buffered = 0;
        isAudioTrackStart = false;
        isPlaying = true;

        while (isPlaying()) {

            VoiceMessageNotify msg = null;

            try {
                msg = mJitterBuffer.jitterTake();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(msg == null) {
                missedFrames ++;
                mJitterBuffer.jitterUpdateDelay();
                mJitterBuffer.jitterTick();
                if(missedFrames > 10) {
                    MLog.w(TAG, "so many missed frames");
                }
                continue;
            }

            mJitterBuffer.jitterUpdateDelay();

            missedFrames = 0;
            amr2Pcm.codec(msg.getMsg(), msg.getYunvaId(),
                    msg.getTroopsId(), msg.getExpand());

            mJitterBuffer.jitterTick();
        }

        audioTrack.flush();
        audioTrack.stop();
        audioTrack.release();

        mJitterBuffer.destroyJitter();

        MLog.d(TAG, "run() finished, stop,release audioTrack");

    }

}
