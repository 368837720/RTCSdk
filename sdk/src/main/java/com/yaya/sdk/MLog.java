package com.yaya.sdk;

import android.util.Log;

/**
 * Created by ober on 2016/10/26.
 */
public class MLog {

    public static final boolean enable = true;


    public static void d(String tag, String msg) {
        if(enable) {
            Log.d(tag, "" + msg);
        }
    }

    public static void v(String tag, String msg) {
        if(enable) {
            Log.v(tag, "" + msg);
        }
    }

    public static void i(String tag, String msg) {
        if(enable) {
            Log.i(tag, "" + msg);
        }
    }

    public static void w(String tag, String msg) {
        if(enable) {
            Log.w(tag, "" + msg);
        }
    }

    public static void e(String tag, String msg) {
        if(enable) {
            Log.e(tag, "" + msg);
        }
    }
}
