package com.yaya.sdk.audio.core;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.media.audiofx.AcousticEchoCanceler;
import android.media.audiofx.NoiseSuppressor;
import android.os.Process;

import com.yaya.sdk.MLog;

/**
 * 录音 task
 * Created by ober on 2016/11/1.
 */
public class AudioRecordTask implements Runnable {

    private static final String TAG = "AudioRecordTask";

    private volatile boolean isRecording = false;

    private AudioRecord audioRecord;

    private static final int SAMPLE_SIZE_IN_HZ = 8000;
    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    private static final int AUDIO_SOURCE = MediaRecorder.AudioSource.MIC;

    private static final int frameSize = 160;
    private short[] readData = new short[frameSize];
    private int bufferSizeInBytes;
    private Echo echo;

    private short[] echoData = new short[160];
    private boolean echoResult;

    private OnRecordListener onRecordListener;

    private AcousticEchoCanceler mSystemAEC;
    private NoiseSuppressor mSystemNS;

    public AudioRecordTask(OnRecordListener listener) {
        this.onRecordListener = listener;
        this.echo = AudioWebrtcTool.m_ec;
        int minBufferSize = AudioRecord.getMinBufferSize(SAMPLE_SIZE_IN_HZ,
                CHANNEL_CONFIG, AUDIO_FORMAT);
        bufferSizeInBytes = minBufferSize * 2;
    }



    private void initAudioRecord() {
        audioRecord = new AudioRecord(AUDIO_SOURCE, SAMPLE_SIZE_IN_HZ,
                CHANNEL_CONFIG, AUDIO_FORMAT, bufferSizeInBytes);
        MLog.i(TAG, "AudioRecord init with MinBufferSizeInBytes=" +bufferSizeInBytes);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            int session = audioRecord.getAudioSessionId();
            if(!AcousticEchoCanceler.isAvailable()) {
                MLog.i(TAG, "System AEC not available");
            } else {
                AcousticEchoCanceler echoCanceler = AcousticEchoCanceler.create(session);
                if(echoCanceler != null) {
                    int result = echoCanceler.setEnabled(true);
                    if (result == 0) {
                        MLog.d(TAG, "AEC created");
                        this.mSystemAEC = echoCanceler;
                    } else {
                        MLog.d(TAG, "AEC disable " + result);
                    }
                }
            }

            if(NoiseSuppressor.isAvailable()) {
                NoiseSuppressor suppressor = NoiseSuppressor.create(session);
                if(suppressor != null) {
                    int result = suppressor.setEnabled(true);
                    if (result == 0) {
                        MLog.d(TAG, "NS created");
                        this.mSystemNS = suppressor;
                    } else {
                        MLog.d(TAG, "NS disable " + result);
                    }
                }
            } else {
                MLog.i(TAG, "System NS not available");
            }


        }
    }

    public synchronized void stopRecording() {
        MLog.d(TAG, "stop recording");
        this.isRecording = false;
    }

    public synchronized boolean isRecording() {
        return isRecording;
    }

    @Override
    public void run() {
        MLog.d(TAG, "run() start tid =" + Thread.currentThread().getId());
        Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO);

        isRecording = true;
        synchronized (AudioWebrtcTool.ECHO_LOCK) {
            if(echo != null) {
                echo.echoReset();
            } else {
                MLog.w(TAG, "null echo canceller");
            }
        }
        initAudioRecord();
        try {
            audioRecord.startRecording();
        } catch (Exception e) {
            e.printStackTrace();
            if(onRecordListener != null) {
                onRecordListener.recordUnavailable(-111);
                return;
            }
        }

//        try {
//            Thread.sleep(40);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        int count = 0;
        if(onRecordListener != null) {
            onRecordListener.recordStart();
        }
        while (isRecording && !Thread.interrupted()) {
            int read;
            try {
                read = audioRecord.read(readData, 0, frameSize);
            } catch (Exception e) {
                e.printStackTrace();
                if(onRecordListener != null) {
                    onRecordListener.recordUnavailable(-112);
                }
                break;
            }

            if(read < 0) {
                MLog.w(TAG, "record.read fail! code=" + read);
                if(count > 10) {
                    // 通知录音设备不可用，请检查权限是否已开启
                    if(onRecordListener != null) {
                        onRecordListener.recordUnavailable(read);
                    }
                    break;
                }
                count ++;
                continue;
            }

            short[] out = new short[frameSize];
            System.arraycopy(readData, 0, out, 0, frameSize);
            synchronized (AudioWebrtcTool.ECHO_LOCK) {
                if(echo != null) {
                    echoResult = echo.echoCancel(out, echoData, false);
                } else {
                    MLog.w(TAG, "logic err, null EchoCanceler");
                    System.arraycopy(out, 0, echoData, 0, echoData.length);
                    echoResult = true;
                }
            }
            if(!echoResult) {
                //静音, 不会进入这里
                MLog.w(TAG, "logic err, vadFlag");
                continue;
            }
            short[] data = new short[frameSize];
            System.arraycopy(echoData, 0, data, 0, frameSize);
            if(onRecordListener != null) {
                //record 偶尔会耗时大(pcm2amr)， 大部分2ms
                onRecordListener.record(Func.shortArray2ByteArray(data));
            }
        }

        try {
            MLog.d(TAG, "stop and release");
            audioRecord.stop();
            audioRecord.release();
            if(mSystemAEC != null) {
                mSystemAEC.setEnabled(false);
                mSystemAEC.release();
            }
            if(mSystemNS != null) {
                mSystemNS.setEnabled(false);
                mSystemNS.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(onRecordListener != null) {
            onRecordListener.recordFinish();
        }

        MLog.d(TAG, "end of run()");
    }
}
