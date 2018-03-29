package com.yaya.sdk.audio;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Build;

import com.yaya.sdk.MLog;
import com.yaya.sdk.YayaRTV;
import com.yaya.sdk.audio.buffer.BlockQueueBuffer;
import com.yaya.sdk.audio.buffer.JitterBufferInterface;
import com.yaya.sdk.audio.core.AudioPlayTask;
import com.yaya.sdk.http.YayaRequestBuilder;
import com.yaya.sdk.tcp.core.YayaTcp;
import com.yaya.sdk.tlv.protocol.message.VoiceMessageNotify;

/**
 * 管理play线程
 * Created by ober on 2016/11/11.
 */
public class PlayManager {

    private static final String TAG = "PlayManager";

    private static PlayManager instance;

    private final AudioPlayTask mAudioPlayTask;

    private final JitterBufferInterface mJitterBuffer;

    private Thread mAudioPlayThread;

    private boolean isNativeOpen; //标识native lib开关，防止重复开关

    public static PlayManager getInstance() {
        if(instance == null) {
            instance = new PlayManager();
        }
        return instance;
    }

    private PlayManager() {
        isNativeOpen = false;
        //mJitterBuffer = YayaNativeJitterBuffer.newInstance();
        mJitterBuffer = BlockQueueBuffer.newInstance();
        mAudioPlayTask = new AudioPlayTask(mJitterBuffer); //voiceMsg consumer
    }

    public void openLib() {
        if(!isNativeOpen) {
            Amr2Pcm.openLib();
            isNativeOpen = true;
        }
    }

    public void closeLib() {
        if(isNativeOpen) {
            Amr2Pcm.closeLib();
            isNativeOpen = false;
        }
    }

    public void destroy() {
        closeLib();
        instance = null;
    }

    public synchronized void startListenAndPlay(VoiceMsgRespDispatcher.VoiceMsgDispatchCallback callback) {
        MLog.d(TAG, "startListenAndPlay");
        if(isPlaying()) {
            MLog.w(TAG, "current is playing, return");
            return;
        }
        openLib();
        mJitterBuffer.initJitter();
        YayaTcp.getInstance().registerSignalDispatcher(VoiceMessageNotify.class,
                new VoiceMsgRespDispatcher(mJitterBuffer, callback)); //voiceMsg producer
        mAudioPlayThread = new Thread(mAudioPlayTask, "AudioPlayThread");
        mAudioPlayThread.start();
        registerHeadsetReceiver();
    }

    public synchronized void stopListenAndPlay() {
        YayaTcp.getInstance().unregisterSignalDispatcher(VoiceMessageNotify.class);
        unregisterHeadsetReceiver();
        if(mAudioPlayTask.isPlaying()) {
            mAudioPlayTask.stop();
        }
        if(mAudioPlayThread != null && mAudioPlayThread.isAlive()) {
            mAudioPlayThread.interrupt();
        }
        mAudioPlayThread = null;
        mJitterBuffer.destroyJitter();
    }

    public synchronized void reset() {
        mJitterBuffer.destroyJitter();
        mJitterBuffer.initJitter();
    }

    public synchronized boolean isPlaying() {
        if(mAudioPlayThread == null) {
            return false;
        }
        return mAudioPlayTask.isPlaying() && mAudioPlayThread.isAlive();
    }

    private void registerHeadsetReceiver() {
        MLog.d(TAG, "registerHeadsetReceiver");
        Context c = ((YayaRTV)(YayaRTV.getInstance())).getContext();
        if(c == null) {
            MLog.w(TAG, "null context");
            return;
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_HEADSET_PLUG);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            filter.addAction(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED);
        }
        c.registerReceiver(mHeadsetReceiver, filter);
    }

    private void unregisterHeadsetReceiver() {
        MLog.d(TAG, "unregisterHeadsetReceiver");
        Context c = ((YayaRTV)(YayaRTV.getInstance())).getContext();
        if(c == null) {
            MLog.w(TAG, "null context");
            return;
        }
        c.unregisterReceiver(mHeadsetReceiver);
    }

    private BroadcastReceiver mHeadsetReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            MLog.d(TAG, "onReceive:" + action);

            boolean shouldSpeakerPhoneOn = false;

            if(Intent.ACTION_HEADSET_PLUG.equals(action)) {
                int isOn = intent.getIntExtra("state", -1);
                MLog.d(TAG, "isOn=" + isOn);
                if(isOn == -1) {
                    return;
                }
                shouldSpeakerPhoneOn = isOn != 1;
            } else if(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED.equals(action)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
                    int state = adapter.getProfileConnectionState(BluetoothProfile.HEADSET);
                    if(state == BluetoothProfile.STATE_CONNECTED) {
                        shouldSpeakerPhoneOn = false;
                    } else if(state == BluetoothProfile.STATE_DISCONNECTED) {
                        shouldSpeakerPhoneOn = true;
                    }
                }
            }

            AudioManager audioManager = (AudioManager) context
                    .getSystemService(Context.AUDIO_SERVICE);
            audioManager.setSpeakerphoneOn(shouldSpeakerPhoneOn);

        }
    };
}
