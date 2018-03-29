package com.yaya.sdk.audio;

import com.yaya.sdk.MLog;
import com.yaya.sdk.audio.core.AudioRecordTask;
import com.yaya.sdk.audio.core.OnRecordListener;
import com.yaya.sdk.config.SdkConfig;
import com.yaya.sdk.tcp.core.TcpSignalBuilder;
import com.yaya.sdk.tcp.core.YayaTcp;
import com.yaya.sdk.tlv.protocol.message.VoiceMessageReq;

/**
 * 管理record线程
 * Created by ober on 2016/11/2.
 */
public class RecordManager {

    public interface RecordExceptionHandler {
        void onRecordException(int code);
    }

    private static final String TAG = "RecordManager";

    private static RecordManager instance;

    private Thread mRecordThread;

    private AudioRecordTask mRecordTask;

    private Pcm2Amr pcm2Amr;

    private volatile boolean isRecording;

    private TcpSignalBuilder mTcpSignalBuilder;
    private String mTroopsId;
    private long mYunvaId;
    private String mExpand;
    private SdkConfig mConfig;
    private String mAppId;
    private RecordExceptionHandler mRecordExceptionHandler;

    private boolean isNativeOpen; //标识native lib开关， 避免重复打开，关闭

    public static RecordManager getInstance() {
        if(instance == null) {
            instance = new RecordManager();
        }
        return instance;
    }

    private RecordManager() {
        mRecordTask = new AudioRecordTask(new LocalOnRecordListener());
        pcm2Amr = new Pcm2Amr(new LocalAmrDataListener());
        isNativeOpen = false;
    }

    public void openLib() {
        if(!isNativeOpen) {
            pcm2Amr.openLib();
            isNativeOpen = true;
        }
    }

    public void closeLib() {
        if(isNativeOpen) {
            pcm2Amr.closeLib();
            isNativeOpen = false;
        }
    }

    public void destroy() {
        closeLib();
        instance = null;
    }

    public synchronized boolean isRecording() {
        return mRecordThread != null
                && mRecordThread.isAlive()
                && mRecordTask.isRecording()
                && isRecording;
    }

    public synchronized void startRecord(long yunvaId,
        String troopsId, String expand, String appId, SdkConfig config,
        RecordExceptionHandler handler) {
        MLog.d(TAG, "startRecord()");
        if(isRecording()) {
            MLog.w(TAG, "current is recording, return");
            return;
        }
        this.mAppId = appId;
        this.mYunvaId = yunvaId;
        this.mTroopsId = troopsId;
        this.mConfig = config;
        this.mExpand = expand;
        openLib();
        mTcpSignalBuilder = TcpSignalBuilder.with(appId, config);
        mRecordExceptionHandler = handler;
        mRecordThread = new Thread(mRecordTask, "AudioRecordThread");
        mRecordThread.start();
    }

    public synchronized void stopRecord() {
        MLog.d(TAG, "stopRecord()");
        if(isRecording()) {
            mRecordTask.stopRecording();
        }
        mRecordExceptionHandler = null;
    }

    private class LocalAmrDataListener implements Pcm2Amr.CodecListener {

        @Override
        public void onFinishCodec(byte[] amrData) {
            //tcp发送
            VoiceMessageReq signal = mTcpSignalBuilder
                    .buildVoiceMsgReq(amrData, mYunvaId, mTroopsId, mExpand);

            //理想间隔100ms
            //MLog.d(TAG, "onFinishCodec " + amrData.length + "," + System.currentTimeMillis());
            YayaTcp.getInstance().getConnection().write(signal);
        }
    }

    private class LocalOnRecordListener implements OnRecordListener {

        @Override
        public void record(byte[] pcmData) {
            //320 byte
            //MLog.d(TAG, "record back " + pcmData.length);
            pcm2Amr.codec(pcmData);
        }

        @Override
        public void recordUnavailable(int read) {
            if(mRecordExceptionHandler != null) {
                mRecordExceptionHandler.onRecordException(read);
            }
        }

        @Override
        public void recordFinish() {
            isRecording = false;
        }

        @Override
        public void recordStart() {
            isRecording = true;
        }
    }

}
