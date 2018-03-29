package com.yaya.sdk.config;

import android.content.Context;
import android.os.Environment;

import com.yaya.sdk.RTV;

/**
 * Created by ober on 2016/10/26.
 */
class EnvironmentSdkConfig implements SdkConfig {

    private final RTV.Env mEnv;

    private final Context mContext;

    EnvironmentSdkConfig(Context context, RTV.Env env) {
        mEnv = env;
        mContext = context;
    }

    @Override
    public String getAccessServer() {
        if(mEnv == RTV.Env.Test) {
            return "http://114.215.169.98:8558/index";
        } else if(mEnv == RTV.Env.Product){
            //return "http://c.yunva.com:8555/index";
            return "http://mixvoice.yunva.com:8555/index";
        } else {
            return "http://u01.aya.yunva.com:8555/index";
        }
    }

    @Override
    public String getAccessInfoPath() {

        final String dirName;
        if(mEnv == RTV.Env.Test) {
            dirName = "/yunva_sdk_test/voice_access";
        } else if(mEnv == RTV.Env.Product) {
            dirName = "/yunva_sdk/voice_access";
        } else {
            dirName = "/yunva_sdk_ov/voice_access";
        }

        final boolean sdCardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);

        if(sdCardExist) {
            return Environment.getExternalStorageDirectory().toString()
                    + dirName;
        } else {
            return mContext.getFilesDir().getAbsolutePath()
                    + dirName;
        }
    }

    @Override
    public String getAccessInfoFileName() {
        return "ticket";
    }

    @Override
    public String getSdkAppId() {
        return "100006";
    }

    @Override
    public String getSdkVersion() {
        return "230";
    }

    @Override
    public String getReleaseTime() {
        return "2016-11-25";
    }
}
