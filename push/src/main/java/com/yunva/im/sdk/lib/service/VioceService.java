package com.yunva.im.sdk.lib.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.yunva.im.sdk.ImSdk;

/**
 * Created by ober on 2016/11/25.
 */
public class VioceService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        ImSdk.a();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
