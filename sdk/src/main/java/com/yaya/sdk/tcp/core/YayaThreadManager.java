package com.yaya.sdk.tcp.core;

import android.os.Build;
import android.os.HandlerThread;
import android.os.Looper;

import com.yaya.sdk.MLog;

/**
 * Created by ober on 2016/11/9.
 */
class YayaThreadManager {
    public static final String TAG = "YayaThreadManager";

    private HandlerThread mHandlerThread;

    YayaThreadManager() {}

    public synchronized void start() {
        MLog.d(TAG, "start");
        if(mHandlerThread != null) {
            if(mHandlerThread.isAlive()) {
                return;
            }
        }
        mHandlerThread = new HandlerThread("YayaThread");
        mHandlerThread.start();
    }

    public synchronized boolean isAlive() {
        if(mHandlerThread == null) {
            return false;
        }
        return mHandlerThread.isAlive();
    }

    public synchronized Looper getYayaLooper() {
        if(mHandlerThread == null) {
            return null;
        }
        return mHandlerThread.getLooper();
    }

    public synchronized void stop() {
        MLog.d(TAG, "stop");
        if(mHandlerThread != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                mHandlerThread.quitSafely();
            } else {
                mHandlerThread.quit();
            }
        }
        mHandlerThread = null;
    }
}
