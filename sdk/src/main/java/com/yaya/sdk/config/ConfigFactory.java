package com.yaya.sdk.config;

import android.content.Context;

import com.yaya.sdk.RTV;

/**
 * Created by ober on 2016/10/26.
 */
public final class ConfigFactory {

    public static SdkConfig buildConfig(Context c, RTV.Env env) {
        if(env == null) {
            env = RTV.Env.Test;
        }
        return new EnvironmentSdkConfig(c, env);
    }
}
